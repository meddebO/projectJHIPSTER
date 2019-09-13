import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { JhipsterAppSharedModule } from 'app/shared';
import {
  GalaxyComponent,
  GalaxyDetailComponent,
  GalaxyUpdateComponent,
  GalaxyDeletePopupComponent,
  GalaxyDeleteDialogComponent,
  galaxyRoute,
  galaxyPopupRoute
} from './';

const ENTITY_STATES = [...galaxyRoute, ...galaxyPopupRoute];

@NgModule({
  imports: [JhipsterAppSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [GalaxyComponent, GalaxyDetailComponent, GalaxyUpdateComponent, GalaxyDeleteDialogComponent, GalaxyDeletePopupComponent],
  entryComponents: [GalaxyComponent, GalaxyUpdateComponent, GalaxyDeleteDialogComponent, GalaxyDeletePopupComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class JhipsterAppGalaxyModule {}
