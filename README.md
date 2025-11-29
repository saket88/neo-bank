# neo-bank
[![CircleCI](https://circleci.com/gh/saket88/neo-bank.svg?style=svg)](https://circleci.com/gh/saket88/neo-bank)

Repository for modern neo online bank

## Overview

This is a modern neo bank application that provides:
1. **Money Transfer**: Facility to transfer money (EUR for now)
2. **India Stack Account Aggregator**: RBI-compliant Account Aggregator framework for secure financial data sharing


1. We are using sparkjava as the micro framework as it is super fast.

2. Intitally Dagger2 was considered for DI but it has some issues post jdk 8. So Guice was fallback choice for this

3. Mockito and Junit are used as testing framework.

4. TDD is used as a development practise.

5. JPA is used as ORM.

6. This is a modular architecture having broad categories of application,infrastructure,entity and e2e test

7. There is no end to end testing framework used for the end to end testing. 
I found POSTMAN collections a good documentation for that. It has been enclosed in the module neo-e2e-test

## India Stack Account Aggregator

> ### ⚠️ IMPORTANT LEGAL NOTICE
> 
> **This Account Aggregator implementation is for DEVELOPMENT, TESTING, and DEMONSTRATION purposes only.**
> 
> **Operating an Account Aggregator in production requires an NBFC-AA license from the Reserve Bank of India (RBI).**
> 
> - ❌ Do NOT use with real customer data without RBI license
> - ❌ Do NOT connect to live banking systems without authorization
> - ✅ Use for sandbox testing, demos, and proof-of-concept only
> 
> **📋 [READ LICENSE REQUIREMENTS →](docs/LICENSE_REQUIREMENTS.md)**
>
> **Estimated Licensing Cost**: ₹5-10 crore ($600K-$1.2M USD)  
> **Timeline**: 18-24 months  
> **Penalties for Non-Compliance**: Criminal prosecution, heavy fines, company shutdown

This implementation includes a complete **Account Aggregator (AA)** framework based on India Stack specifications. 

### Features
- ✅ **Consent Management**: Complete consent lifecycle (create, approve, revoke)
- ✅ **Account Discovery**: Discover and link accounts from multiple banks
- ✅ **FI Requests**: Request financial information with user consent
- ✅ **RBI Compliance**: Follows RBI NBFC-AA guidelines
- ✅ **Security**: End-to-end encryption, digital signatures, OAuth 2.0

### Documentation

#### 🚀 Getting Started
- **[Quick Start Guide](docs/QUICK_START.md)** - Do I need a license? Start here!
- **[Sandbox Testing](docs/SANDBOX_MODE.md)** - Safe testing without license

#### ⚖️ Legal & Compliance  
- **[License Requirements](docs/LICENSE_REQUIREMENTS.md)** - RBI licensing guide ⚠️

#### 📖 Technical Documentation
- **[API Specification](docs/api-specs/india-stack-account-aggregator-openapi.yaml)** - OpenAPI 3.0 spec
- **[Account Aggregator Spec](docs/ACCOUNT_AGGREGATOR_SPEC.md)** - Detailed API guide
- **[Implementation Guide](docs/IMPLEMENTATION_GUIDE.md)** - Technical implementation
- **[India Stack Overview](docs/INDIA_STACK_OVERVIEW.md)** - Ecosystem & business context

#### 🧪 Testing
- **[Postman Collection](docs/postman/AA-API-Collection.json)** - API testing collection

### Account Aggregator Endpoints

#### Consent Management
- `POST /api/v1/aa/Consent` - Create consent request
- `GET /api/v1/aa/Consent/:id` - Get consent details
- `PUT /api/v1/aa/Consent/:id` - Update consent (approve/reject/revoke)
- `GET /api/v1/aa/Consent/handle/:handle` - Get consent by handle

#### Account Discovery
- `POST /api/v1/aa/Accounts/discover` - Discover customer accounts at FIP
- `POST /api/v1/aa/Accounts/link` - Link discovered accounts
- `GET /api/v1/aa/Accounts` - Get linked accounts

#### FI Requests
- `POST /api/v1/aa/FI/request` - Create FI data request
- `GET /api/v1/aa/FI/request/:sessionId` - Get FI request status

## Steps to run

1. mvn clean install
2. java -jar neo-application/target/neo-application-1.0-SNAPSHOT.jar 

## Testing

### Money Transfer
Please use the POSTMAN collections in the neo-e2e-test for a basic transfer flow.

### Account Aggregator
Import the Postman collection from `docs/postman/AA-API-Collection.json` to test AA APIs.
