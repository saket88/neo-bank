# India Stack Account Aggregator API Specification

## Overview

This document describes the implementation of RBI-compliant Account Aggregator (AA) APIs for the Neo Bank platform, enabling secure financial data sharing as per India Stack specifications.

## What is Account Aggregator?

Account Aggregator is a new type of RBI-regulated entity (NBFC-AA) that helps users securely and digitally access and share information from one financial institution to another regulated financial institution in the AA network.

### Key Principles

1. **Consent-Based**: User must explicitly consent to data sharing
2. **Encrypted**: End-to-end encryption ensures data security
3. **Digital**: Completely digital flow, no paper trail
4. **Time-Bound**: Consents are time-limited and revocable
5. **Purpose-Specific**: Each consent specifies the purpose of data access

## Architecture

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│             │         │                  │         │             │
│  Customer   │◄───────►│  Account         │◄───────►│    FIP      │
│             │         │  Aggregator (AA) │         │   (Banks)   │
└─────────────┘         └──────────────────┘         └─────────────┘
                               ▲
                               │
                               ▼
                        ┌─────────────┐
                        │             │
                        │    FIU      │
                        │ (FinTechs)  │
                        └─────────────┘
```

## Actors

### 1. Account Aggregator (AA)
- Licensed NBFC-AA entity
- Manages consent lifecycle
- Routes data requests between FIP and FIU
- Does NOT store financial data

### 2. Financial Information Provider (FIP)
- Banks, NBFCs, Mutual Funds, Insurance companies
- Holds customer's financial data
- Provides data based on approved consent

### 3. Financial Information User (FIU)
- FinTech apps, lending platforms
- Requests customer's financial data
- Uses data for providing services

### 4. Customer
- End user who owns financial accounts
- Grants/revokes consent
- Controls data sharing

## API Flows

### 1. User Registration Flow

```
Customer → AA: POST /User/register
AA → Customer: Registration Success + Customer ID (mobile@aa)
```

### 2. Account Discovery & Linking Flow

```
Customer → AA: POST /Accounts/discover
AA → FIP: Forward discovery request
FIP → AA: Return masked accounts
AA → Customer: Show discovered accounts
Customer → AA: POST /Accounts/link (with OTP/auth)
AA → FIP: Verify and link accounts
FIP → AA: Link confirmation
AA → Customer: Linked accounts
```

### 3. Consent Creation & Approval Flow

```
FIU → AA: POST /Consent (Consent request)
AA → Customer: Consent notification (via app/SMS)
Customer → AA: GET /Consent/handle/{handle}
Customer → AA: PUT /Consent/{id} (Approve/Reject)
AA → FIU: Consent status notification
```

### 4. Financial Data Fetch Flow

```
FIU → AA: POST /FI/request (with consent ID)
AA → FIP: Forward FI request
FIP → FIP: Prepare encrypted data
FIP → AA: POST /FI/Notification (data ready)
FIU → AA: POST /FI/fetch (with session ID)
AA → FIP: Fetch encrypted FI data
FIP → AA: Return encrypted data
AA → FIU: Forward encrypted FI data
FIU → FIU: Decrypt using shared keys
```

## Data Types (FI Types)

The AA framework supports various financial information types:

| FI Type | Description |
|---------|-------------|
| DEPOSIT | Savings/Current accounts |
| TERM_DEPOSIT | Fixed deposits |
| RECURRING_DEPOSIT | Recurring deposits |
| SIP | Systematic Investment Plans |
| EQUITIES | Stock holdings |
| MUTUAL_FUNDS | Mutual fund investments |
| BONDS | Bond holdings |
| INSURANCE_POLICIES | Insurance policies |
| NPS | National Pension System |
| GSTR1_3B | GST returns (for businesses) |

## Purpose Codes

RBI-defined purpose codes for data access:

| Code | Purpose |
|------|---------|
| 101 | Wealth management service |
| 102 | Customer spending patterns, budget or other reportings |
| 103 | Aggregated statement |
| 104 | Explicit consent for monitoring of the accounts |
| 105 | Explicit one-time consent for accessing information |
| 106 | Explicit consent for listing of accounts |

## Security Features

### 1. End-to-End Encryption
- Uses ECDH (Elliptic Curve Diffie-Hellman) key exchange
- Curve25519 for key generation
- AES-256-GCM for data encryption
- Financial data encrypted at FIP, decrypted at FIU
- AA never sees unencrypted financial data

### 2. Digital Signatures
- All consent objects digitally signed
- Uses X.509 certificates
- Ensures non-repudiation

### 3. OAuth 2.0
- JWT-based authentication
- Short-lived access tokens
- Refresh token rotation

### 4. API Security
- HTTPS/TLS 1.2+ mandatory
- Request signing
- Timestamp validation (max 5 min skew)
- Transaction ID for idempotency

## Consent Lifecycle

```
PENDING → ACTIVE → PAUSED → ACTIVE
                  ↓         ↓
                REVOKED  EXPIRED
                  ↓
              REJECTED
```

### States:
- **PENDING**: Awaiting customer approval
- **ACTIVE**: Approved and valid
- **PAUSED**: Temporarily paused by customer
- **REVOKED**: Permanently revoked by customer
- **EXPIRED**: Validity period ended
- **REJECTED**: Rejected by customer

## Data Filters

Consents can specify filters for data:

### Transaction Amount Filter
```json
{
  "type": "TRANSACTIONAMOUNT",
  "operator": ">=",
  "value": "10000"
}
```

### Transaction Type Filter
```json
{
  "type": "TRANSACTIONTYPE",
  "operator": "=",
  "value": "CREDIT"
}
```

## Sample Request/Response

### Creating a Consent Request

**Request:**
```json
POST /Consent
{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:30:00Z",
  "txnid": "f35761ac-4a18-11e8-96ff-0277a9fbfedc",
  "ConsentDetail": {
    "consentStart": "2024-01-15T00:00:00Z",
    "consentExpiry": "2024-12-31T23:59:59Z",
    "consentMode": "VIEW",
    "fetchType": "PERIODIC",
    "consentTypes": ["TRANSACTIONS", "PROFILE", "SUMMARY"],
    "fiTypes": ["DEPOSIT"],
    "DataConsumer": {
      "id": "FIU-001",
      "type": "FIU"
    },
    "Customer": {
      "id": "9876543210@neobank"
    },
    "Purpose": {
      "code": "101",
      "text": "Wealth management service"
    },
    "FIDataRange": {
      "from": "2023-01-01T00:00:00Z",
      "to": "2024-01-15T00:00:00Z"
    },
    "DataLife": {
      "unit": "MONTH",
      "value": 6
    },
    "Frequency": {
      "unit": "MONTH",
      "value": 1
    }
  }
}
```

**Response:**
```json
{
  "ver": "1.0",
  "timestamp": "2024-01-15T10:30:01Z",
  "txnid": "f35761ac-4a18-11e8-96ff-0277a9fbfedc",
  "Customer": {
    "id": "9876543210@neobank"
  },
  "ConsentHandle": "5caa78f1-3e85-4a5c-98b7-b40f6e0c4f4d"
}
```

### Fetching Financial Information

**Request:**
```json
POST /FI/fetch
{
  "ver": "1.0",
  "timestamp": "2024-01-15T11:00:00Z",
  "txnid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "sessionId": "b8c9d0e1-f2a3-4b5c-6d7e-8f9012345678",
  "fipId": "HDFC-FIP",
  "linkRefNumber": ["XXXX-XXXX-1234"]
}
```

**Response:**
```json
{
  "ver": "1.0",
  "timestamp": "2024-01-15T11:00:02Z",
  "txnid": "a1b2c3d4-e5f6-7890-abcd-ef1234567890",
  "FI": [
    {
      "fipID": "HDFC-FIP",
      "data": [
        {
          "linkRefNumber": "XXXX-XXXX-1234",
          "maskedAccNumber": "XXXXXX1234",
          "encryptedFI": "eyJhbGciOiJFQ0RILUVTIiwiZW5jIjoiQTI1NkdDTSJ9..."
        }
      ],
      "KeyMaterial": {
        "cryptoAlg": "ECDH",
        "curve": "Curve25519",
        "params": "",
        "DHPublicKey": {
          "expiry": "2024-01-15T12:00:00Z",
          "Parameters": "Curve25519",
          "KeyValue": "MCowBQYDK2VuAyEAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX="
        },
        "Nonce": "abcdef123456"
      }
    }
  ]
}
```

## Error Codes

| Error Code | Description |
|------------|-------------|
| InvalidRequest | Request validation failed |
| Unauthorized | Authentication failed |
| NoSuchConsent | Consent ID not found |
| ConsentExpired | Consent has expired |
| ConsentRevoked | Consent was revoked |
| InvalidConsentStatus | Operation not allowed in current consent state |
| NoDataAvailable | No data available for requested period |
| FIPNotAvailable | FIP service unavailable |
| EncryptionError | Data encryption/decryption failed |
| InvalidSignature | Digital signature verification failed |
| RateLimitExceeded | API rate limit exceeded |

## Compliance & Regulations

### RBI Guidelines
- Master Direction on NBFC-Account Aggregator (Reserve Bank) Directions, 2016
- IT Framework for NBFC-Account Aggregator, 2020
- AA Technical Standards v1.1.3

### Data Retention
- AA MUST NOT store financial information
- Consent logs must be maintained for 10 years
- Audit trails for all data access

### Security Standards
- ISO 27001 certification required
- Regular security audits
- Incident reporting to RBI within 6 hours

## Testing

### Sandbox Environment
```
Base URL: https://sandbox.neobank.in/aa/v1
```

### Test Credentials
Available in the developer portal after registration.

### Postman Collection
Import the Postman collection from: `/docs/postman/AA-API-Collection.json`

## Rate Limits

| Endpoint | Rate Limit |
|----------|------------|
| POST /Consent | 100 requests/hour per FIU |
| POST /FI/request | 500 requests/hour per FIU |
| POST /FI/fetch | 1000 requests/hour per FIU |
| GET /Consent/{id} | 1000 requests/hour per FIU |

## Support

### Developer Portal
https://developer.neobank.in/aa

### API Documentation
https://api.neobank.in/aa/docs

### Contact
- Email: aa-support@neobank.in
- Phone: 1800-XXX-XXXX

## Versioning

Current Version: **v1.0**

- API versioning follows semantic versioning
- Breaking changes will result in major version increment
- Backward compatibility maintained for at least 6 months

## Changelog

### v1.0.0 (2024-01-15)
- Initial release
- Consent management APIs
- FI request and fetch APIs
- Account discovery and linking
- User registration and authentication

## References

1. [Sahamati - Account Aggregator Standards](https://api.sahamati.org.in/)
2. [RBI Master Direction on NBFC-AA](https://www.rbi.org.in/)
3. [India Stack Documentation](https://indiastack.org/)
4. [DEPA (Data Empowerment and Protection Architecture)](https://depa.world/)

## License

Copyright © 2024 Neo Bank India. All rights reserved.

This API specification is provided for authorized developers and partners only.
