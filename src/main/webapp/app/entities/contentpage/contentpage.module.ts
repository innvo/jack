import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { JackSharedModule } from 'app/shared';
import {
  ContentpageComponent,
  ContentpageDetailComponent,
  ContentpageUpdateComponent,
  ContentpageDeletePopupComponent,
  ContentpageDeleteDialogComponent,
  contentpageRoute,
  contentpagePopupRoute
} from './';

const ENTITY_STATES = [...contentpageRoute, ...contentpagePopupRoute];

@NgModule({
  imports: [JackSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    ContentpageComponent,
    ContentpageDetailComponent,
    ContentpageUpdateComponent,
    ContentpageDeleteDialogComponent,
    ContentpageDeletePopupComponent
  ],
  entryComponents: [ContentpageComponent, ContentpageUpdateComponent, ContentpageDeleteDialogComponent, ContentpageDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JackContentpageModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
