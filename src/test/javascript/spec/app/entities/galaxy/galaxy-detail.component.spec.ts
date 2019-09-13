/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterAppTestModule } from '../../../test.module';
import { GalaxyDetailComponent } from 'app/entities/galaxy/galaxy-detail.component';
import { Galaxy } from 'app/shared/model/galaxy.model';

describe('Component Tests', () => {
  describe('Galaxy Management Detail Component', () => {
    let comp: GalaxyDetailComponent;
    let fixture: ComponentFixture<GalaxyDetailComponent>;
    const route = ({ data: of({ galaxy: new Galaxy(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterAppTestModule],
        declarations: [GalaxyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(GalaxyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(GalaxyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.galaxy).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
