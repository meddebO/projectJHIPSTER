import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGalaxy } from 'app/shared/model/galaxy.model';

@Component({
  selector: 'jhi-galaxy-detail',
  templateUrl: './galaxy-detail.component.html'
})
export class GalaxyDetailComponent implements OnInit {
  galaxy: IGalaxy;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ galaxy }) => {
      this.galaxy = galaxy;
    });
  }

  previousState() {
    window.history.back();
  }
}
