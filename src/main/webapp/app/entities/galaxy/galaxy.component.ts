import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGalaxy } from 'app/shared/model/galaxy.model';
import { AccountService } from 'app/core';
import { GalaxyService } from './galaxy.service';

@Component({
  selector: 'jhi-galaxy',
  templateUrl: './galaxy.component.html'
})
export class GalaxyComponent implements OnInit, OnDestroy {
  galaxies: IGalaxy[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected galaxyService: GalaxyService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.galaxyService
      .query()
      .pipe(
        filter((res: HttpResponse<IGalaxy[]>) => res.ok),
        map((res: HttpResponse<IGalaxy[]>) => res.body)
      )
      .subscribe(
        (res: IGalaxy[]) => {
          this.galaxies = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInGalaxies();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IGalaxy) {
    return item.id;
  }

  registerChangeInGalaxies() {
    this.eventSubscriber = this.eventManager.subscribe('galaxyListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
