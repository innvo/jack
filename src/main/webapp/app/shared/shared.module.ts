import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { JackSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [JackSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [JackSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JackSharedModule {
  static forRoot() {
    return {
      ngModule: JackSharedModule
    };
  }
}
