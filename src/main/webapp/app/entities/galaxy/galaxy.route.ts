import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Galaxy } from 'app/shared/model/galaxy.model';
import { GalaxyService } from './galaxy.service';
import { GalaxyComponent } from './galaxy.component';
import { GalaxyDetailComponent } from './galaxy-detail.component';
import { GalaxyUpdateComponent } from './galaxy-update.component';
import { GalaxyDeletePopupComponent } from './galaxy-delete-dialog.component';
import { IGalaxy } from 'app/shared/model/galaxy.model';

@Injectable({ providedIn: 'root' })
export class GalaxyResolve implements Resolve<IGalaxy> {
  constructor(private service: GalaxyService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGalaxy> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Galaxy>) => response.ok),
        map((galaxy: HttpResponse<Galaxy>) => galaxy.body)
      );
    }
    return of(new Galaxy());
  }
}

export const galaxyRoute: Routes = [
  {
    path: '',
    component: GalaxyComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Galaxies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: GalaxyDetailComponent,
    resolve: {
      galaxy: GalaxyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Galaxies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: GalaxyUpdateComponent,
    resolve: {
      galaxy: GalaxyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Galaxies'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: GalaxyUpdateComponent,
    resolve: {
      galaxy: GalaxyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Galaxies'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const galaxyPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: GalaxyDeletePopupComponent,
    resolve: {
      galaxy: GalaxyResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'Galaxies'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
