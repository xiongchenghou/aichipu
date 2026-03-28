import { Module } from '@nestjs/common';
import { HttpModule } from '@nestjs/axios';
import { MetalRecycleService } from './metal-recycle.service';
import { MetalRecycleController } from './metal-recycle.controller';

@Module({
  imports: [HttpModule],
  controllers: [MetalRecycleController],
  providers: [MetalRecycleService],
})
export class MetalRecycleModule {}
