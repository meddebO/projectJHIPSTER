import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { IGalaxy, Galaxy } from 'app/shared/model/galaxy.model';
import { GalaxyService } from './galaxy.service';

@Component({
  selector: 'jhi-galaxy-update',
  templateUrl: './galaxy-update.component.html'
})
export class GalaxyUpdateComponent implements OnInit {
  isSaving: boolean;

  editForm = this.fb.group({
    id: [],
    name: [],
    type: []
  });

  constructor(protected galaxyService: GalaxyService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ galaxy }) => {
      this.updateForm(galaxy);
    });
  }

  updateForm(galaxy: IGalaxy) {
    this.editForm.patchValue({
      id: galaxy.id,
      name: galaxy.name,
      type: galaxy.type
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const galaxy = this.createFromForm();
    if (galaxy.id !== undefined) {
      this.subscribeToSaveResponse(this.galaxyService.update(galaxy));
    } else {
      this.subscribeToSaveResponse(this.galaxyService.create(galaxy));
    }
  }

  private createFromForm(): IGalaxy {
    return {
      ...new Galaxy(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      type: this.editForm.get(['type']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGalaxy>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
}
