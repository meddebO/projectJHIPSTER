/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { JhipsterAppTestModule } from '../../../test.module';
import { GalaxyUpdateComponent } from 'app/entities/galaxy/galaxy-update.component';
import { GalaxyService } from 'app/entities/galaxy/galaxy.service';
import { Galaxy } from 'app/shared/model/galaxy.model';

describe('Component Tests', () => {
  describe('Galaxy Management Update Component', () => {
    let comp: GalaxyUpdateComponent;
    let fixture: ComponentFixture<GalaxyUpdateComponent>;
    let service: GalaxyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterAppTestModule],
        declarations: [GalaxyUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(GalaxyUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GalaxyUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GalaxyService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Galaxy(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Galaxy();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
