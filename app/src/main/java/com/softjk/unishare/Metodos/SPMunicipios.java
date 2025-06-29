package com.softjk.unishare.Metodos;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.softjk.unishare.R;
import com.softjk.unishare.Regist.RegistroUni;

public class SPMunicipios {

    public static void SelecciMuni(Spinner sp, Spinner Municipio, Context context){

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                switch (selectedItem) {

                    case "Aguascalientes":
                        String [] Opciones = {"Seleccione su Municipio","Aguascalientes","Asientos","Calvillo","El dorado","El Llano","Jesús María","Lomas",
                                "Pabellón de Arteaga","Rincón de Romos","San Francisco de los Romo","Otro Municipio"};
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,Opciones);
                        Municipio.setAdapter(adapter);
                        break;

                    case "Baja-California":
                        String [] OpcionesBCalif = {"Seleccione su Municipio","Ensenada","Mexicali","Playas de Rosarito","San Quintín","Tecate","Tijuana","Otro Municipio"};
                        ArrayAdapter<String> adapterBCalif = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesBCalif);
                        Municipio.setAdapter(adapterBCalif);
                        break;

                    case "Baja-California-Sur":
                        String [] OpcionesBCaliSur = {"Seleccione su Municipio","Comondú","La Paz","Loreto","Los Cabos","Mulegé","Otro Municipio"};
                        ArrayAdapter<String> adapterBajCalSur = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesBCaliSur);
                        Municipio.setAdapter(adapterBajCalSur);
                        break;

                    case "Campeche":
                        String [] OpcionesCamp = {"Seleccione su Municipio","Calakmul","Calkini","Campeche","Carmen","Champotón","China","Escárcega",
                                "Hecelchakan","Xpujil","Otro Municipio"};
                        ArrayAdapter<String> adapterCamp = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesCamp);
                        Municipio.setAdapter(adapterCamp);
                        break;

                    case "CDMX":
                        String [] OpcionesCDMX = {"Seleccione su Municipio","Álvaro Obregón","Azcapotzalco","Benito Juárez","Coyoacán","Cuajimalpa de Morelos","Cuauhtémoc"
                                ,"Gustavo A. Madero","Iztacalco","Iztapalapa","La Magdalena Contreras","Miguel Hidalgo","Milpa Alta","Tláhuac","Tlalpan","Venustiano Carranza",
                                "Xochimilco","Otro Municipio"};
                        ArrayAdapter<String> adapterCDMX = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesCDMX);
                        Municipio.setAdapter(adapterCDMX);
                        break;

                    case "Chiapas":
                        String [] OpcionesChia = {"Seleccione su Municipio","Altamirano","Amatenango del Valle","Arriaga","Bella Vista","Chenalhó","Chilón",
                                "Cintalapa","Comitán de Domínguez","Escuintla","Frontera Comalapa","Huehuetán","Huixtla","Ixtapa","Larráinzar","Las Margaritas",
                                "Motozintla","Ocosingo","Ocotepec","Ocozocoautla de Espinosa","Ostuacán","Palenque","Pantepec","Pichucalco","Pijijiapan","Pueblo Nuevo Solistahuacán",
                                "Salto de Agua","San Cristóbal de las Casas","Suchiapa","Tapachula","Teopisca","Tonalá","Tuxtla Chico","Tuxtla Gutiérrez","Tuzantán","Venustiano Carranza",
                                "Villa Corzo","Villaflores","Otro Municipio"};
                        ArrayAdapter<String> adapterChia = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesChia);
                        Municipio.setAdapter(adapterChia);
                        break;

                    case "Chihuahua":
                        String [] OpcionesChihu = {"Seleccione su Municipio","Bocoyna","Camargo","Casas Grandes","Chihuahua","Cuauhtémoc","Delicias",
                                "Guachochi","Guadalupe y Calvo","Hidalgo del Parral","Jiménez","Juárez","Madera","Nuevo Casas Grandes","Parral","Saucillo",
                                "Urique","Otro Municipio"};
                        ArrayAdapter<String> adapterChihu = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesChihu);
                        Municipio.setAdapter(adapterChihu);
                        break;

                    case "Coahuila":
                        String [] OpcionesCoa = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterCoa = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesCoa);
                        Municipio.setAdapter(adapterCoa);
                        break;

                    case "Colima":
                        String [] OpcionesColi = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterColi = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesColi);
                        Municipio.setAdapter(adapterColi);
                        break;

                    case "Durango":
                        String [] OpcionesDur = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterDur = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesDur);
                        Municipio.setAdapter(adapterDur);
                        break;

                    case "EstadoMx":
                        String [] OpcionesEsMx = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterEsMx = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesEsMx);
                        Municipio.setAdapter(adapterEsMx);
                        break;

                    case "Guanajuato":
                        String [] OpcionesGua = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterGua = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesGua);
                        Municipio.setAdapter(adapterGua);
                        break;

                    case "Guerrero":
                        String [] OpcionesGue = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterGue = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesGue);
                        Municipio.setAdapter(adapterGue);
                        break;

                    case "Jalisco":
                        String [] OpcionesJa = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterJa = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesJa);
                        Municipio.setAdapter(adapterJa);
                        break;

                    case "Michoacan":
                        String [] OpcionesMic = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterMic = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesMic);
                        Municipio.setAdapter(adapterMic);
                        break;

                    case "Morelos":
                        String [] OpcionesMor = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterMor = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesMor);
                        Municipio.setAdapter(adapterMor);
                        break;

                    case "Nayarit":
                        String [] OpcionesNay = {"Seleccione su Municipio","Opcio1","Otro Municipio"};
                        ArrayAdapter<String> adapterNay = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesNay);
                        Municipio.setAdapter(adapterNay);
                        break;

                    case "Puebla":
                        String [] OpcionesPuebla = {"Seleccione su Municipio"," Acatlán","Acatzingo","Ajalpan","Amozoc","Atlixco","Chalchicomula De S.","Chiautla","Chignahuapan",
                                "Chilchotla","Cuatlancingo","Cuetzalan","Huauchinango","Huehuetla","Huejotzingo","Izúcar Matamoros","Juan Bonilla","Libres","Nicolás Bravo",
                                "CD. Puebla","S. N. De Los Ranchos","San Andrés Cholula","San M. Texmelucan","San Pedro Cholula","Tecamachalco","Tecomatlán","Tehuacán","Tepeaca","Tepexi De Rodríguez","Tlatlauquitepec",
                                "Tetela De Ocampo","Teziutlán", "Venust. Carranza","Xicotepec De Juárez","Zacapoaxtla","Zacatlán","Zaragoza","Zautla","Otro Municipio"};

                        ArrayAdapter<String> adapterMuni = new ArrayAdapter<>(context, R.layout.spinner_item_estilo,OpcionesPuebla);
                        Municipio.setAdapter(adapterMuni);
                        break;

                    case "Hidalgo":
                        String [] OpcionesHidal = {"Seleccione su Municipio","Actopan","Apan","Huejutla","Huichapan","Ixmiquilpan","La loma","Mineral de la Reforma","Mixquiahuala",
                                "P. Obregon","Pachuca","Sahagún","Tepeji del Río de Ocampo","Tezontepec","Tizayuca","Tlahuelilpan","Tlaxiaca","Tolcayuca","Tula",
                                "Tulancingo","Zacualtipán de Ángeles","Zempoala","Zimapan","Otro Municipio"};

                        ArrayAdapter<String> adapterHidal = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesHidal);
                        Municipio.setAdapter(adapterHidal);
                        break;

                    case "Oaxaca":
                        String [] OpcionesOax = {"Seleccione su Municipio","Ciudad De Tlaxiaco","Ciudad Ixtepec","Cuilapan Guerrero",
                                "Heroica C. Tlaxiaco","Huajuapan De León","Ixtlán De Juárez","Juchitán Zaragoza","Loma Bonita","Miahuatlán P. Díaz",
                                " Oaxaca De Juárez","Ocotlán De Morelos","Pinotepa Nacional","S Dom. Tehuantepec","S. Cruz Xoxocotlán",
                                "S. María Atzompa", "S. María Huatulco", "Salina Cruz", "San Bart. Tuxtepec", "San Jacinto Amilpas",
                                "San Juan B. Tuxtepec","San Juan C. Mixe","San Mig. El Grande","San P. Comitancillo", "San Pedro Mixtepec",
                                "San Pedro Pochutla","San Sebastián Tutla","Santa María Tla", "T. De Segura Y Luna", "Teotitlán F Magón",
                                "Teposcolula","Otro Municipio"};
                        ArrayAdapter<String> adapterOax = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesOax);
                        Municipio.setAdapter(adapterOax);
                        break;

                    case "Veracruz":
                        String [] OpcionesVer = {"Seleccione su Municipio","Acayucan","Álamo Temapache","Alvarado","Boca del Río","Camerino Mendoza","Chicontepec","Coatzacoalcos","Córdoba",
                                "Cerro Azul","Cosamaloapan","Coscomatepec","Cuitláhuac","Espinal","Huayacocotla","Huastusco","Ixhuatlán de Madero","Jesús Carranza","J. Rodríguez Clara",
                                "La Antigua","Las Choapas","Mart. De La Torre","Minatitlan","Mecayapan","Medellín","Misantla","Naranjos","Nogales","Pánuco","Orizaba",
                                "Papantla","Perote", "Poza Rica de Hidalgo","Rafael Lucio","Río Blanco","San Andrés Tuxtla","Tantoyoca","Tequila","Tierra Blanca","Tuxpan","Tlacotalpan",
                                "Tres Valles","Veracruz","Villa Oluta","Úrsulo Galvan","Xalapa","Zongolica","Otro Municipio"};

                        ArrayAdapter<String> adapterVer = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesVer);
                        Municipio.setAdapter(adapterVer);
                        break;

                    case "Nuevo-Leon":
                        String [] OpcionesLeo = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterLeo = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesLeo);
                        Municipio.setAdapter(adapterLeo);
                        break;

                    case "Queretaro":
                        String [] OpcionesQuer = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterQuer = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesQuer);
                        Municipio.setAdapter(adapterQuer);
                        break;

                    case "Quintana-Roo":
                        String [] OpcionesQui = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterQui = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesQui);
                        Municipio.setAdapter(adapterQui);
                        break;

                    case "San-Luis-Potosi":
                        String [] OpcionesPoto = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterPoto = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesPoto);
                        Municipio.setAdapter(adapterPoto);
                        break;

                    case "Sinaloa":
                        String [] OpcionesSin = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterSin = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesSin);
                        Municipio.setAdapter(adapterSin);
                        break;

                    case "Sonora":
                        String [] OpcionesSon = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterSon = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesSon);
                        Municipio.setAdapter(adapterSon);
                        break;

                    case "Tabasco":
                        String [] OpcionesTab = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterTab = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesTab);
                        Municipio.setAdapter(adapterTab);
                        break;

                    case "Tamaulipas":
                        String [] OpcionesTam = {"Seleccione su Municipio","Aldama", "Altamira","CD. Madero","El Mante","Guerrero",
                        "La Pesca","Matamoros","Miguel Alemán","Nuevo Ladero", "Reynosa", "Río Bravo", "San Fernando","Tampico",
                        "Valle Hermoso","Victoria","Otro Municipio"};
                        ArrayAdapter<String> adapterTam = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesTam);
                        Municipio.setAdapter(adapterTam);
                        break;

                    case "Tlaxcala":
                        String [] OpcionesTax = {"Seleccione su Municipio","Apizaco","Calpulalpan","Contla j. Cuamatzi","Huamantla","Ixtacuixtla","Panotla",
                                "San Pablo del Monte","Tepayanco","Tlaxcala","Totolac","Tzompantepec","Yauquemecan","Zacatelco","Otro Municipio"};
                        ArrayAdapter<String> adapterTax = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesTax);
                        Municipio.setAdapter(adapterTax);
                        break;

                    case "Yucatan":
                        String [] OpcionesYu = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterYu = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesYu);
                        Municipio.setAdapter(adapterYu);
                        break;

                    case "Zacatecas":
                        String [] OpcionesZa = {"No Disponible","Otro Municipio"};
                        ArrayAdapter<String> adapterZa = new ArrayAdapter<>(context,R.layout.spinner_item_estilo,OpcionesZa);
                        Municipio.setAdapter(adapterZa);
                        break;

                    default:
                        Toast.makeText(context, "Por Favor Selecciona su Estado", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
