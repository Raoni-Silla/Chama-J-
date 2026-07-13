import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { PrestadorResponseDTO } from '../../../DTOS/Prestador/PrestadorResponseDTO.dto';
import { ActivatedRoute } from '@angular/router';
import { Navbarlogged } from '../../../components/navbarlogged/navbarlogged';
import { CommonModule } from '@angular/common';
import { PrestadorService } from '../../../service/prestador-service';
import { ProgressSpinnerModule } from 'primeng/progressspinner';

@Component({
  selector: 'app-perfil-prestador',
  imports: [Navbarlogged, CommonModule, ProgressSpinnerModule],
  templateUrl: './perfil-prestador.html',
  styleUrl: './perfil-prestador.css',
})
export class PerfilPrestador implements OnInit {


  prestadorId: number | null = null;
  prestador: PrestadorResponseDTO | null = null; 
  carregando: boolean = true;

 

  constructor(
    private route: ActivatedRoute, 
    private prestadorService : PrestadorService,
    private cdr : ChangeDetectorRef) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.prestadorId = Number(id);
        this.buscarDetalhesDoPrestador(this.prestadorId);
      }
    });
  }

  buscarDetalhesDoPrestador(id: number) {
    this.carregando = true;
    this.prestadorService.buscarPrestadorPorId(id).subscribe({
      next: (dados) => {
        this.prestador = dados;
        console.log(this.prestador)
        this.carregando = false;
        this.cdr.detectChanges();
      },
      error: (erro) => {
        console.error('Erro ao buscar perfil do prestador', erro);
        this.carregando = false;    
      }
    });
  }

}
