import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AbaCartoes } from './aba-cartoes';

describe('AbaCartoes', () => {
  let component: AbaCartoes;
  let fixture: ComponentFixture<AbaCartoes>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AbaCartoes]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AbaCartoes);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
