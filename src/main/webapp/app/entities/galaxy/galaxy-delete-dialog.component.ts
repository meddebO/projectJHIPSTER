import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGalaxy } from 'app/shared/model/galaxy.model';
import { GalaxyService } from './galaxy.service';

@Component({
  selector: 'jhi-galaxy-delete-dialog',
  templateUrl: './galaxy-delete-dialog.component.html'
})
export class GalaxyDeleteDialogComponent {
  galaxy: IGalaxy;

  constructor(protected galaxyService: GalaxyService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.galaxyService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'galaxyListModification',
        content: 'Deleted an galaxy'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-galaxy-delete-popup',
  template: ''
})
export class GalaxyDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ galaxy }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(GalaxyDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.galaxy = galaxy;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/galaxy', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/galaxy', { outlets: { popup: null } }]);
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
