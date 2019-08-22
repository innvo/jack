import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Contentpage } from 'app/shared/model/contentpage.model';
import { ContentpageService } from './contentpage.service';
import { ContentpageComponent } from './contentpage.component';
import { ContentpageDetailComponent } from './contentpage-detail.component';
import { ContentpageUpdateComponent } from './contentpage-update.component';
import { ContentpageDeletePopupComponent } from './contentpage-delete-dialog.component';
import { IContentpage } from 'app/shared/model/contentpage.model';

@Injectable({ providedIn: 'root' })
export class ContentpageResolve implements Resolve<IContentpage> {
  constructor(private service: ContentpageService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IContentpage> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Contentpage>) => response.ok),
        map((contentpage: HttpResponse<Contentpage>) => contentpage.body)
      );
    }
    return of(new Contentpage());
  }
}

export const contentpageRoute: Routes = [
  {
    path: '',
    component: ContentpageComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'jackApp.contentpage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ContentpageDetailComponent,
    resolve: {
      contentpage: ContentpageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jackApp.contentpage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ContentpageUpdateComponent,
    resolve: {
      contentpage: ContentpageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jackApp.contentpage.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ContentpageUpdateComponent,
    resolve: {
      contentpage: ContentpageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jackApp.contentpage.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const contentpagePopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ContentpageDeletePopupComponent,
    resolve: {
      contentpage: ContentpageResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'jackApp.contentpage.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
