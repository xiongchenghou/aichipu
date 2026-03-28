import { Body, Controller, Get, Param, Post, Put } from '@nestjs/common';
import { MetalRecycleService, CustomerPayload, ProductPayload, ReceiptPayload, ShipmentPayload, FinancialPayload } from './metal-recycle.service';

@Controller('metal-recycle')
export class MetalRecycleController {
  constructor(private readonly metalRecycleService: MetalRecycleService) {}

  @Get('customers')
  listCustomers() {
    return this.metalRecycleService.listCustomers();
  }

  @Post('customers')
  createCustomer(@Body() payload: CustomerPayload) {
    return this.metalRecycleService.createCustomer(payload);
  }

  @Put('customers/:id')
  updateCustomer(@Param('id') id: string, @Body() payload: CustomerPayload) {
    return this.metalRecycleService.updateCustomer(id, payload);
  }

  @Get('products')
  listProducts() {
    return this.metalRecycleService.listProducts();
  }

  @Post('products')
  createProduct(@Body() payload: ProductPayload) {
    return this.metalRecycleService.createProduct(payload);
  }

  @Put('products/:id')
  updateProduct(@Param('id') id: string, @Body() payload: ProductPayload) {
    return this.metalRecycleService.updateProduct(id, payload);
  }

  @Get('inventory')
  inventorySnapshot() {
    return this.metalRecycleService.inventorySnapshot();
  }

  @Post('receipts')
  recordReceipt(@Body() payload: ReceiptPayload) {
    return this.metalRecycleService.recordReceipt(payload);
  }

  @Post('shipments')
  recordShipment(@Body() payload: ShipmentPayload) {
    return this.metalRecycleService.recordShipment(payload);
  }

  @Get('transactions')
  transactionHistory() {
    return this.metalRecycleService.transactionHistory();
  }

  @Post('finance/transactions')
  recordFinancial(@Body() payload: FinancialPayload) {
    return this.metalRecycleService.recordFinancial(payload);
  }

  @Get('finance/transactions')
  financialTransactions() {
    return this.metalRecycleService.financialTransactions();
  }

  @Get('finance/summary')
  financialSummary() {
    return this.metalRecycleService.financialSummary();
  }
}
