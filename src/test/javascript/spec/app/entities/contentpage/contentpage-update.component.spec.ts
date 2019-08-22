/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JackTestModule } from '../../../test.module';
import { ContentpageUpdateComponent } from 'app/entities/contentpage/contentpage-update.component';
import { ContentpageService } from 'app/entities/contentpage/contentpage.service';
import { Contentpage } from 'app/shared/model/contentpage.model';

describe('Component Tests', () => {
  describe('Contentpage Management Update Component', () => {
    let comp: ContentpageUpdateComponent;
    let fixture: ComponentFixture<ContentpageUpdateComponent>;
    let service: ContentpageService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JackTestModule],
        declarations: [ContentpageUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(ContentpageUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ContentpageUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContentpageService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contentpage(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Contentpage();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
