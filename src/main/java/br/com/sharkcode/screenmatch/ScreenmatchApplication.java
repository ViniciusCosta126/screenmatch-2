package br.com.sharkcode.screenmatch;

import br.com.sharkcode.screenmatch.models.DadosSerie;
import br.com.sharkcode.screenmatch.services.ConsumoApi;
import br.com.sharkcode.screenmatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ScreenmatchApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        var consumoApi = new ConsumoApi();
        var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=230c22ac");
        System.out.println(json);

        ConverteDados conversor = new ConverteDados();

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        System.out.println(dados);
    }
}
