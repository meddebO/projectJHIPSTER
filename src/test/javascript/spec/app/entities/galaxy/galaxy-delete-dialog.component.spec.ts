/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { JhipsterAppTestModule } from '../../../test.module';
import { GalaxyDeleteDialogComponent } from 'app/entities/galaxy/galaxy-delete-dialog.component';
import { GalaxyService } from 'app/entities/galaxy/galaxy.service';

describe('Component Tests', () => {
  describe('Galaxy Management Delete Component', () => {
    let comp: GalaxyDeleteDialogComponent;
    let fixture: ComponentFixture<GalaxyDeleteDialogComponent>;
    let service: GalaxyService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterAppTestModule],
        declarations: [GalaxyDeleteDialogComponent]
      })
        .overrideTemplate(GalaxyDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GalaxyDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GalaxyService);
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
