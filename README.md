# Japanese Metal Recycling Management Platform

This repository contains a Spring Boot back-office service and a NestJS edge/BFF service that together deliver
core capabilities for a Japanese metal recycling plant. The implementation covers:

- inbound receipt management for buying scrap metal
- outbound shipment management for sales orders
- perpetual inventory tracking with weighted average costing
- customer and product master data maintenance
- financial ledger capture for revenue, expense and adjustment entries

## Project layout

| Path | Description |
| --- | --- |
| `src/main/java` | Spring Boot application code with REST controllers, services and domain models. |
| `nest-app` | NestJS gateway that proxies browser/API requests to the Spring Boot backend. |

## Running the Spring Boot backend

```bash
mvn spring-boot:run
```

The service exposes JSON endpoints under `http://localhost:8080/api/metal-recycle`.

## Running the NestJS gateway

```bash
cd nest-app
npm install
npm run start:dev
```

The gateway listens on port `3001` by default and proxies to the Spring Boot service. Override the backend URL by
setting `BACKEND_URL`, for example:

```bash
BACKEND_URL=http://localhost:8080/api/metal-recycle npm run start:dev
```

## Example API usage

Record a receipt:

```bash
curl -X POST http://localhost:8080/api/metal-recycle/receipts \
  -H 'Content-Type: application/json' \
  -d '{
    "productId": "<product-id>",
    "quantity": 100,
    "unitCost": 45000,
    "supplierName": "札幌金属商事",
    "referenceNumber": "PO-20240401"
  }'
```

Retrieve the financial summary:

```bash
curl http://localhost:8080/api/metal-recycle/finance/summary
```

These endpoints can be orchestrated from the NestJS gateway or consumed directly by client applications.
