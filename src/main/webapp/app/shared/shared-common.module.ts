import { NgModule } from '@angular/core';

import { JhipsterAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent } from './';

@NgModule({
  imports: [JhipsterAppSharedLibsModule],
  declarations: [JhiAlertComponent, JhiAlertErrorComponent],
  exports: [JhipsterAppSharedLibsModule, JhiAlertComponent, JhiAlertErrorComponent]
})
export class JhipsterAppSharedCommonModule {}
