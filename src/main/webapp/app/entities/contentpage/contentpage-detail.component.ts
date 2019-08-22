import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IContentpage } from 'app/shared/model/contentpage.model';

@Component({
  selector: 'jhi-contentpage-detail',
  templateUrl: './contentpage-detail.component.html'
})
export class ContentpageDetailComponent implements OnInit {
  contentpage: IContentpage;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ contentpage }) => {
      this.contentpage = contentpage;
    });
  }

  previousState() {
    window.history.back();
  }
}
