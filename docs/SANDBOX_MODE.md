# Sandbox Mode - Safe Testing Without License

## Overview

This guide explains how to safely use the Account Aggregator implementation for testing, development, and demonstration purposes **WITHOUT** requiring an RBI license.

## ⚠️ What You CAN and CANNOT Do

### ✅ PERMITTED (No License Required)

1. **Development & Testing**
   - Run the application locally
   - Test APIs with mock data
   - Unit and integration testing
   - Performance testing with dummy data

2. **Demonstrations**
   - Show features to stakeholders
   - Investor presentations
   - Technical proof-of-concept
   - Training sessions

3. **Sandbox Environment**
   - Isolated test environment
   - Mock FIP responses
   - Simulated user flows
   - API exploration with Postman

### ❌ PROHIBITED (License Required)

1. **Production Use**
   - Real customer data
   - Live banking systems
   - Public-facing services
   - Commercial operations

2. **Real Financial Data**
   - Actual bank accounts
   - Real transaction history
   - Customer PII (Personally Identifiable Information)
   - Live FIP connections

## 🛠️ Setting Up Sandbox Mode

### 1. Configuration

Create a sandbox configuration file:

**File**: `neo-application/src/main/resources/sandbox.properties`

```properties
# Sandbox Mode Configuration
sandbox.mode=true
sandbox.environment=development

# Mock Data Settings
mock.data.enabled=true
mock.fip.enabled=true
mock.customer.data=true

# Security Settings (Relaxed for testing)
auth.required=false
jwt.validation.enabled=false
rate.limiting.enabled=false

# Database (Use in-memory H2)
db.type=h2
db.mode=memory

# Logging
log.level=DEBUG
log.requests=true
log.responses=true

# Warning Banner
display.sandbox.warning=true
```

### 2. Mock Data Generation

Create mock data generators for testing:

**Sample Mock Customer**:
```json
{
  "customerId": "SANDBOX001@neobank",
  "mobile": "9999999999",
  "name": "Test Customer",
  "email": "test@sandbox.neobank.in",
  "pan": "XXXXX9999X",
  "status": "ACTIVE"
}
```

**Sample Mock Accounts**:
```json
{
  "accounts": [
    {
      "linkRefNumber": "MOCK-LINK-001",
      "maskedAccNumber": "XXXXXX1111",
      "accType": "SAVINGS",
      "fiType": "DEPOSIT",
      "fipId": "MOCK-BANK-001",
      "fipName": "Mock Bank Limited",
      "branch": "Sandbox Branch",
      "balance": 50000.00
    },
    {
      "linkRefNumber": "MOCK-LINK-002",
      "maskedAccNumber": "XXXXXX2222",
      "accType": "CURRENT",
      "fiType": "DEPOSIT",
      "fipId": "MOCK-BANK-002",
      "fipName": "Test Bank Corporation",
      "branch": "Demo Branch",
      "balance": 100000.00
    }
  ]
}
```

**Sample Mock Transactions**:
```json
{
  "transactions": [
    {
      "txnId": "MOCK-TXN-001",
      "date": "2024-01-15",
      "type": "CREDIT",
      "amount": 5000.00,
      "narration": "Salary Credit - Test Corp"
    },
    {
      "txnId": "MOCK-TXN-002",
      "date": "2024-01-14",
      "type": "DEBIT",
      "amount": 1500.00,
      "narration": "ATM Withdrawal"
    }
  ]
}
```

### 3. Environment Variables

Set environment variables for sandbox mode:

**Linux/Mac**:
```bash
export SANDBOX_MODE=true
export MOCK_DATA_ENABLED=true
export SKIP_RBI_CHECKS=true
export DISPLAY_WARNING=true
```

**Windows**:
```cmd
set SANDBOX_MODE=true
set MOCK_DATA_ENABLED=true
set SKIP_RBI_CHECKS=true
set DISPLAY_WARNING=true
```

### 4. Starting in Sandbox Mode

```bash
# Build the application
mvn clean install -DskipTests

# Run with sandbox profile
java -jar \
  -Dsandbox.mode=true \
  -Dmock.data.enabled=true \
  neo-application/target/neo-application-1.0-SNAPSHOT.jar
```

You should see a warning banner on startup:

```
╔═══════════════════════════════════════════════════════════╗
║                                                           ║
║                  ⚠️  SANDBOX MODE ACTIVE ⚠️                ║
║                                                           ║
║   This is a TESTING environment with MOCK data only.     ║
║                                                           ║
║   DO NOT use with real customer data.                    ║
║   DO NOT connect to live banking systems.                ║
║   RBI license required for production use.               ║
║                                                           ║
╚═══════════════════════════════════════════════════════════╝
```

## 🧪 Testing Scenarios

### Scenario 1: Account Discovery

**Test Flow**:
```
1. Create mock customer
2. Discover accounts at mock FIP
3. Verify masked account numbers
4. Check account types and balances
```

**Postman Request**:
```http
POST http://localhost:9080/api/v1/aa/Accounts/discover
Content-Type: application/json

{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:00:00Z",
  "txnid": "sandbox-discover-001",
  "fipId": "MOCK-BANK-001",
  "customer": {
    "id": "SANDBOX001@neobank",
    "identifiers": [
      {
        "type": "MOBILE",
        "value": "9999999999"
      }
    ]
  }
}
```

**Expected Response**:
```json
{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:00:01Z",
  "txnid": "sandbox-discover-001",
  "customer": {
    "accounts": [
      {
        "linkRefNumber": "MOCK-LINK-001",
        "maskedAccNumber": "XXXXXX1111",
        "accType": "SAVINGS",
        "fiType": "DEPOSIT",
        "branch": "Sandbox Branch",
        "fiDataFetchStatus": "AVAILABLE"
      }
    ]
  }
}
```

### Scenario 2: Consent Creation

**Test Flow**:
```
1. FIU creates consent request
2. Get consent handle
3. Customer views consent
4. Customer approves consent
5. Verify consent status
```

**Postman Request**:
```http
POST http://localhost:9080/api/v1/aa/Consent
Content-Type: application/json

{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:30:00Z",
  "txnid": "sandbox-consent-001",
  "consentDetail": {
    "consentStart": "2024-01-15T00:00:00Z",
    "consentExpiry": "2024-12-31T23:59:59Z",
    "consentMode": "VIEW",
    "fetchType": "PERIODIC",
    "consentTypes": ["TRANSACTIONS", "PROFILE"],
    "fiTypes": ["DEPOSIT"],
    "dataConsumer": {
      "id": "MOCK-FIU-001",
      "type": "FIU"
    },
    "customer": {
      "id": "SANDBOX001@neobank"
    },
    "purpose": {
      "code": "101",
      "text": "Testing wealth management service"
    },
    "fiDataRange": {
      "from": "2023-01-01T00:00:00Z",
      "to": "2024-01-15T00:00:00Z"
    },
    "dataLife": {
      "unit": "MONTH",
      "value": 6
    },
    "frequency": {
      "unit": "MONTH",
      "value": 1
    }
  }
}
```

### Scenario 3: FI Data Request

**Test Flow**:
```
1. Create FI request with approved consent
2. Check session status
3. Receive mock encrypted data
4. Verify data structure
```

### Scenario 4: Complete End-to-End Flow

Use the Postman collection "Complete Flow Example" to test:
1. Account Discovery
2. Account Linking
3. Consent Creation
4. Consent Approval
5. FI Request
6. Data Fetch

## 🎭 Mock FIP Simulator

### Creating Mock FIP Responses

For realistic testing, simulate different FIP behaviors:

#### Success Scenario
```json
{
  "status": "SUCCESS",
  "accounts": [...],
  "responseTime": "200ms"
}
```

#### Delayed Response
```json
{
  "status": "PENDING",
  "message": "Processing request",
  "expectedDelay": "30s"
}
```

#### Error Scenario
```json
{
  "status": "ERROR",
  "errorCode": "FIP_UNAVAILABLE",
  "message": "Bank service temporarily unavailable"
}
```

#### Partial Success
```json
{
  "status": "PARTIAL_SUCCESS",
  "accounts": [
    {
      "linkRefNumber": "MOCK-001",
      "status": "SUCCESS"
    },
    {
      "linkRefNumber": "MOCK-002",
      "status": "FAILED",
      "error": "Account not found"
    }
  ]
}
```

## 🔐 Security in Sandbox Mode

### Disabled Security Features (Safe for Testing)

1. **Authentication**: Bypassed for easy testing
2. **JWT Validation**: Disabled
3. **Rate Limiting**: Disabled
4. **IP Whitelisting**: Disabled
5. **Request Signing**: Optional

### Enabled Security Features (Good Practice)

1. **HTTPS**: Still recommended locally with self-signed cert
2. **Input Validation**: Always enabled
3. **SQL Injection Prevention**: Always enabled
4. **XSS Protection**: Always enabled

### Local HTTPS Setup (Optional)

Generate self-signed certificate:
```bash
keytool -genkeypair \
  -alias sandbox \
  -keyalg RSA \
  -keysize 2048 \
  -keystore sandbox-keystore.jks \
  -validity 365 \
  -dname "CN=localhost, OU=Sandbox, O=NeoBank, L=Mumbai, ST=Maharashtra, C=IN"
```

## 📊 Monitoring & Logging

### Enable Detailed Logging

**logback.xml** configuration:
```xml
<configuration>
  <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
    </encoder>
  </appender>
  
  <logger name="com.bank.api.aa" level="DEBUG"/>
  <logger name="com.bank.services" level="DEBUG"/>
  
  <root level="INFO">
    <appender-ref ref="STDOUT"/>
  </root>
</configuration>
```

### Request/Response Logging

Log all API calls for debugging:
```
[10:30:15.123] [http-nio-9080-exec-1] INFO  c.b.api.aa.ConsentResource - 
  Received consent request: txnid=sandbox-consent-001
[10:30:15.456] [http-nio-9080-exec-1] DEBUG c.b.api.aa.ConsentResource - 
  Request body: {"ver":"1.0","timestamp":"2024-01-15T10:30:00Z",...}
[10:30:15.789] [http-nio-9080-exec-1] INFO  c.b.api.aa.ConsentResource - 
  Consent created: consentHandle=abc123-def456
```

## 🧹 Cleanup After Testing

### Clear Mock Data

```bash
# Stop application
# Delete H2 database files
rm -rf *.db

# Clear logs
rm -rf logs/*.log

# Reset configuration
git checkout neo-application/src/main/resources/application.properties
```

### Database Reset

For H2 in-memory database, simply restart the application.

For persistent H2:
```sql
DROP ALL OBJECTS;
```

## 🎓 Learning Resources

### Use Sandbox Mode To:

1. **Learn AA Concepts**
   - Understand consent lifecycle
   - Explore FI data structures
   - Learn about account linking

2. **Test Integrations**
   - Develop FIU applications
   - Test API clients
   - Validate data flows

3. **Develop Features**
   - Add new functionality
   - Test edge cases
   - Performance optimization

4. **Training**
   - Train development team
   - Demo to stakeholders
   - Customer support training

## 🚀 Moving from Sandbox to Production

### Prerequisites

Before moving to production:

1. ✅ Obtain RBI NBFC-AA license
2. ✅ ISO 27001 certification
3. ✅ Production infrastructure ready
4. ✅ Security audit completed
5. ✅ Compliance team in place
6. ✅ Customer support setup
7. ✅ Legal agreements signed
8. ✅ Insurance coverage obtained

### Configuration Changes

```properties
# Production Configuration
sandbox.mode=false
mock.data.enabled=false
mock.fip.enabled=false

# Enable all security
auth.required=true
jwt.validation.enabled=true
rate.limiting.enabled=true
request.signing.required=true

# Production database
db.type=postgresql
db.url=jdbc:postgresql://prod-db:5432/neobank_aa
db.ssl=true

# Production logging
log.level=INFO
log.sensitive.data=false
audit.enabled=true
```

### Checklist

- [ ] RBI license obtained and active
- [ ] Sandbox mode disabled
- [ ] Mock data disabled
- [ ] Production database configured
- [ ] Security features enabled
- [ ] SSL/TLS certificates installed
- [ ] Monitoring and alerting setup
- [ ] Backup and disaster recovery tested
- [ ] Compliance documentation ready
- [ ] Customer onboarding process defined
- [ ] Support team trained
- [ ] Legal terms and privacy policy published

## ⚠️ Final Warning

**Remember**: Sandbox mode is for testing ONLY. Never:
- Use real customer data in sandbox
- Connect to live banking systems
- Share sandbox URLs publicly
- Store sensitive information
- Deploy sandbox config to production
- Market as production-ready service

**For Production Use**: [Read License Requirements](LICENSE_REQUIREMENTS.md)

## 📞 Support

For sandbox testing issues:
- Check logs in `logs/` directory
- Review Postman collection examples
- Consult API documentation
- Use mock data generators

For production licensing:
- Email: dnbr@rbi.org.in
- Website: https://www.rbi.org.in

---

**Happy Sandbox Testing! 🧪**

Remember: This is a safe environment for learning and development. Enjoy exploring the Account Aggregator framework without any legal risks!
