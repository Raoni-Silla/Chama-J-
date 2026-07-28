import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaPerfilPublico } from './aba-perfil-publico';

describe('AbaPerfilPublico', () => {
  let component: AbaPerfilPublico;
  let fixture: ComponentFixture<AbaPerfilPublico>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbaPerfilPublico]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbaPerfilPublico);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
