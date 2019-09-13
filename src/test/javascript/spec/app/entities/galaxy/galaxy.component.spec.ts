/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterAppTestModule } from '../../../test.module';
import { GalaxyComponent } from 'app/entities/galaxy/galaxy.component';
import { GalaxyService } from 'app/entities/galaxy/galaxy.service';
import { Galaxy } from 'app/shared/model/galaxy.model';

describe('Component Tests', () => {
  describe('Galaxy Management Component', () => {
    let comp: GalaxyComponent;
    let fixture: ComponentFixture<GalaxyComponent>;
    let service: GalaxyService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterAppTestModule],
        declarations: [GalaxyComponent],
        providers: []
      })
        .overrideTemplate(GalaxyComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GalaxyComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(GalaxyService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Galaxy(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.galaxies[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
