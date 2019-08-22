import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IContentpage } from 'app/shared/model/contentpage.model';
import { ContentpageService } from './contentpage.service';

@Component({
  selector: 'jhi-contentpage-delete-dialog',
  templateUrl: './contentpage-delete-dialog.component.html'
})
export class ContentpageDeleteDialogComponent {
  contentpage: IContentpage;

  constructor(
    protected contentpageService: ContentpageService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.contentpageService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'contentpageListModification',
        content: 'Deleted an contentpage'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-contentpage-delete-popup',
  template: ''
})
export class ContentpageDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contentpage }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ContentpageDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.contentpage = contentpage;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/contentpage', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/contentpage', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
