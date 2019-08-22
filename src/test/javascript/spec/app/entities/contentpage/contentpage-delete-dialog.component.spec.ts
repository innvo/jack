/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JackTestModule } from '../../../test.module';
import { ContentpageDeleteDialogComponent } from 'app/entities/contentpage/contentpage-delete-dialog.component';
import { ContentpageService } from 'app/entities/contentpage/contentpage.service';

describe('Component Tests', () => {
  describe('Contentpage Management Delete Component', () => {
    let comp: ContentpageDeleteDialogComponent;
    let fixture: ComponentFixture<ContentpageDeleteDialogComponent>;
    let service: ContentpageService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JackTestModule],
        declarations: [ContentpageDeleteDialogComponent]
      })
        .overrideTemplate(ContentpageDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ContentpageDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ContentpageService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
