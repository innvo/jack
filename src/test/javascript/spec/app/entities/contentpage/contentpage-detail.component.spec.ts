/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JackTestModule } from '../../../test.module';
import { ContentpageDetailComponent } from 'app/entities/contentpage/contentpage-detail.component';
import { Contentpage } from 'app/shared/model/contentpage.model';

describe('Component Tests', () => {
  describe('Contentpage Management Detail Component', () => {
    let comp: ContentpageDetailComponent;
    let fixture: ComponentFixture<ContentpageDetailComponent>;
    const route = ({ data: of({ contentpage: new Contentpage(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JackTestModule],
        declarations: [ContentpageDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(ContentpageDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContentpageDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.contentpage).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
