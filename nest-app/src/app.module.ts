import { Module } from '@nestjs/common';
import { MetalRecycleModule } from './metal-recycle/metal-recycle.module';

@Module({
  imports: [MetalRecycleModule],
})
export class AppModule {}
