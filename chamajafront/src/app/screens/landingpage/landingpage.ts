import { Component } from '@angular/core';
import { Navbar } from '../../components/navbar/navbar';
import { Cardinfo } from '../../components/cardinfo/cardinfo';
import { Defaultbutton } from '../../components/defaultbutton/defaultbutton';
import { Floatingcard } from '../../components/floatingcard/floatingcard';
import { CardInformationMini } from '../../components/card-information-mini/card-information-mini';
import { CardInformationPlus } from '../../components/card-information-plus/card-information-plus';
import { CardAvaliation } from '../../components/card-avaliation/card-avaliation';
import { Callaction } from '../../components/callaction/callaction';
import { Footer } from '../../components/footer/footer';

@Component({
  selector: 'app-landingpage',
  imports: [
    Navbar,
    Cardinfo,
    Defaultbutton,
    Floatingcard,
    CardInformationMini,
    CardInformationPlus,
    CardAvaliation,
    Callaction,
    Footer,
  ],
  templateUrl: './landingpage.html',
  styleUrl: './landingpage.css',
})
export class Landingpage {
}
