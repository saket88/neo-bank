# Account Aggregator Implementation Guide

## Overview

This guide provides technical details for implementing the India Stack Account Aggregator framework in the Neo Bank application.

## Project Structure

```
neo-bank/
├── docs/
│   ├── api-specs/
│   │   └── india-stack-account-aggregator-openapi.yaml  # OpenAPI 3.0 specification
│   ├── postman/
│   │   └── AA-API-Collection.json                       # Postman collection for testing
│   ├── ACCOUNT_AGGREGATOR_SPEC.md                       # Detailed API documentation
│   └── IMPLEMENTATION_GUIDE.md                          # This file
├── neo-entity/
│   └── src/main/java/com/bank/domain/aa/
│       ├── Consent.java                                 # Consent entity
│       ├── ConsentStatus.java                           # Consent status enum
│       ├── FIRequest.java                               # FI Request entity
│       ├── SessionStatus.java                           # Session status enum
│       ├── LinkedAccount.java                           # Linked account entity
│       ├── AccountLinkStatus.java                       # Account link status enum
│       ├── AACustomer.java                              # AA Customer entity
│       └── CustomerStatus.java                          # Customer status enum
└── neo-infrastructure/
    └── src/main/java/com/bank/
        ├── api/aa/
        │   ├── ConsentResource.java                     # Consent API endpoints
        │   ├── FIRequestResource.java                   # FI Request API endpoints
        │   └── AccountDiscoveryResource.java            # Account discovery endpoints
        └── model/aa/
            ├── ConsentRequestDto.java                   # Consent request DTO
            ├── ConsentResponseDto.java                  # Consent response DTO
            ├── ConsentDetailDto.java                    # Consent detail DTO
            ├── FIRequestDto.java                        # FI Request DTO
            ├── FIRequestResponseDto.java                # FI Request response DTO
            ├── AccountDiscoveryRequestDto.java          # Account discovery request DTO
            └── AccountDiscoveryResponseDto.java         # Account discovery response DTO
```

## Architecture

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         API Layer (Resources)           │  ← Spark Java Routes
├─────────────────────────────────────────┤
│      Service Layer (Business Logic)     │  ← To be implemented
├─────────────────────────────────────────┤
│    Repository Layer (Data Access)       │  ← To be implemented
├─────────────────────────────────────────┤
│         Entity Layer (Domain)           │  ← Domain models
└─────────────────────────────────────────┘
```

## Core Components

### 1. Entity Layer (neo-entity)

Domain entities representing core business objects:

#### Consent
Represents a consent given by customer for data sharing.
- **Fields**: consentId, status, customerId, fiuId, validity dates, data ranges, etc.
- **Status**: PENDING → ACTIVE → PAUSED/REVOKED/EXPIRED

#### LinkedAccount
Represents a customer's account linked with the AA.
- **Fields**: linkRefNumber, customerId, fipId, maskedAccNumber, etc.
- **Status**: ACTIVE, INACTIVE, DELINKED, PENDING

#### FIRequest
Represents a request to fetch financial information.
- **Fields**: sessionId, consentId, dataRanges, keyMaterial, etc.
- **Status**: PENDING → ACTIVE → COMPLETED/EXPIRED/FAILED

#### AACustomer
Represents a customer registered with the AA.
- **Fields**: customerId (mobile@aa), name, mobile, email, pan, etc.

### 2. API Layer (neo-infrastructure)

#### ConsentResource
Handles consent lifecycle APIs:
- `POST /api/v1/aa/Consent` - Create consent request
- `GET /api/v1/aa/Consent/:id` - Get consent details
- `PUT /api/v1/aa/Consent/:id` - Update consent status
- `GET /api/v1/aa/Consent/handle/:handle` - Get consent by handle

#### FIRequestResource
Handles FI data request APIs:
- `POST /api/v1/aa/FI/request` - Create FI request
- `GET /api/v1/aa/FI/request/:sessionId` - Get FI request status

#### AccountDiscoveryResource
Handles account discovery and linking:
- `POST /api/v1/aa/Accounts/discover` - Discover accounts at FIP
- `POST /api/v1/aa/Accounts/link` - Link discovered accounts
- `GET /api/v1/aa/Accounts` - Get linked accounts

## Implementation Steps

### Phase 1: Basic Structure (COMPLETED)
✅ OpenAPI specification
✅ Entity models
✅ DTOs for API layer
✅ Basic API endpoints (skeleton)
✅ Documentation
✅ Postman collection

### Phase 2: Service Layer (TODO)
Implement business logic:
- ConsentService - Manage consent lifecycle
- FIRequestService - Handle FI data requests
- AccountDiscoveryService - Account discovery and linking
- AACustomerService - Customer management
- EncryptionService - Handle ECDH key exchange and AES encryption

### Phase 3: Repository Layer (TODO)
Implement data persistence:
- ConsentRepository - Consent CRUD operations
- LinkedAccountRepository - Account linking CRUD
- FIRequestRepository - FI request tracking
- AACustomerRepository - Customer data management

### Phase 4: Security Implementation (TODO)
- JWT authentication
- Digital signature verification
- End-to-end encryption (ECDH + AES-256-GCM)
- Request signing and validation
- Rate limiting

### Phase 5: Integration (TODO)
- FIP integration (mock or real)
- FIU webhook notifications
- SMS/Email notifications for consent
- Audit logging

## Key Technical Decisions

### 1. Date/Time Handling
- Using `LocalDateTime` for timestamp fields
- All timestamps in ISO 8601 format
- Time zone: UTC for API communication

### 2. ID Generation
- UUIDs for consentId, sessionId, txnid
- Format: `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx`

### 3. API Versioning
- URL versioning: `/api/v1/aa/...`
- Version in request body: `"ver": "1.0"`

### 4. Response Format
All responses follow standard structure:
```json
{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:30:00Z",
  "txnid": "uuid",
  "data": { ... }
}
```

### 5. Error Handling
Standard error response:
```json
{
  "errorCode": "ErrorCode",
  "errorMsg": "Human readable message",
  "timestamp": "2024-01-15T10:30:00Z",
  "txnid": "uuid"
}
```

## Security Implementation

### End-to-End Encryption Flow

1. **Key Exchange (ECDH)**
   ```
   FIU generates key pair → Sends public key to AA
   AA forwards to FIP → FIP generates key pair
   Both derive shared secret using ECDH
   ```

2. **Data Encryption (AES-256-GCM)**
   ```
   FIP encrypts FI data with shared secret
   Encrypted data sent through AA to FIU
   FIU decrypts with shared secret
   ```

3. **Key Material Structure**
   ```json
   {
     "cryptoAlg": "ECDH",
     "curve": "Curve25519",
     "DHPublicKey": {
       "expiry": "2024-01-15T12:00:00Z",
       "KeyValue": "base64_encoded_public_key"
     },
     "Nonce": "random_nonce"
   }
   ```

### Digital Signatures

All consent objects must be digitally signed:
- Algorithm: RSA with SHA-256
- Certificate: X.509
- Signature format: Base64 encoded

## Testing

### Unit Testing
Test individual components:
```bash
mvn test
```

### Integration Testing
Use Postman collection:
1. Import `docs/postman/AA-API-Collection.json`
2. Set environment variables
3. Run collection

### End-to-End Testing
Complete flow:
1. Register customer
2. Discover accounts
3. Link accounts
4. Create consent
5. Approve consent
6. Request FI data
7. Fetch encrypted data
8. Decrypt data

## Performance Considerations

### Rate Limiting
- Consent creation: 100 req/hour per FIU
- FI requests: 500 req/hour per FIU
- FI fetch: 1000 req/hour per FIU

### Caching
- Consent status: Cache for 5 minutes
- Linked accounts: Cache for 1 hour
- Session status: Cache for 2 minutes

### Database Indexing
Required indexes:
- consent.consentId (primary)
- consent.customerId (index)
- consent.consentHandle (unique)
- linked_account.customerId (index)
- fi_request.sessionId (primary)
- fi_request.consentId (index)

## Deployment

### Configuration
Environment variables:
```properties
# AA Configuration
AA_ID=neobank
AA_URL=https://api.neobank.in/aa/v1

# Security
JWT_SECRET=<secret>
ENCRYPTION_KEY=<key>
SIGNING_CERTIFICATE=<cert>

# Database
DB_URL=jdbc:postgresql://localhost:5432/neobank_aa
DB_USER=aa_user
DB_PASSWORD=<password>

# Integrations
FIP_REGISTRY_URL=https://fip-registry.sahamati.org.in
FIU_CALLBACK_TIMEOUT=30000
```

### Database Setup
```sql
CREATE TABLE consent (
    consent_id UUID PRIMARY KEY,
    consent_handle VARCHAR(255) UNIQUE,
    status VARCHAR(20),
    customer_id VARCHAR(255),
    fiu_id VARCHAR(255),
    consent_start TIMESTAMP,
    consent_expiry TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    -- other fields
);

CREATE TABLE linked_account (
    link_ref_number VARCHAR(255) PRIMARY KEY,
    customer_id VARCHAR(255),
    fip_id VARCHAR(255),
    masked_acc_number VARCHAR(255),
    status VARCHAR(20),
    linked_date TIMESTAMP,
    -- other fields
);

CREATE TABLE fi_request (
    session_id UUID PRIMARY KEY,
    consent_id UUID,
    fiu_id VARCHAR(255),
    session_status VARCHAR(20),
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    -- other fields
);

CREATE TABLE aa_customer (
    customer_id VARCHAR(255) PRIMARY KEY,
    mobile VARCHAR(10) UNIQUE,
    name VARCHAR(255),
    email VARCHAR(255),
    pan VARCHAR(10),
    status VARCHAR(20),
    registered_at TIMESTAMP
);
```

## Compliance Checklist

- [ ] RBI NBFC-AA license obtained
- [ ] ISO 27001 certification
- [ ] Data retention policy (10 years for logs)
- [ ] Incident reporting mechanism
- [ ] Audit trail implementation
- [ ] Customer grievance redressal
- [ ] Privacy policy and terms of service
- [ ] KYC compliance
- [ ] Regular security audits
- [ ] Penetration testing

## Monitoring & Logging

### Metrics to Track
- API response times
- Success/failure rates
- Consent approval rates
- Active consents
- FI fetch volumes
- Error rates by type

### Logging
All operations must be logged:
- Timestamp
- Transaction ID
- Customer ID
- Operation type
- Result (success/failure)
- Error details (if any)

## Support & Resources

### India Stack Resources
- [Sahamati API Documentation](https://api.sahamati.org.in/)
- [RBI Master Direction](https://www.rbi.org.in/)
- [Account Aggregator Ecosystem](https://sahamati.org.in/)

### Development Tools
- OpenAPI/Swagger Editor
- Postman for API testing
- JMeter for load testing
- SonarQube for code quality

## Next Steps

1. **Implement Service Layer**
   - Create service classes with business logic
   - Add validation
   - Implement encryption/decryption

2. **Add Database Support**
   - Create JPA entities
   - Implement repositories
   - Add transaction management

3. **Security Enhancement**
   - Implement JWT authentication
   - Add request signing
   - Implement rate limiting

4. **Testing**
   - Write unit tests
   - Add integration tests
   - Perform security testing

5. **Documentation**
   - API documentation
   - Developer guides
   - Operations runbook

## Contributing

When adding new features:
1. Update OpenAPI specification
2. Update entity models if needed
3. Create/update DTOs
4. Implement API endpoints
5. Add unit tests
6. Update Postman collection
7. Update documentation

## License

Copyright © 2024 Neo Bank India. All rights reserved.
