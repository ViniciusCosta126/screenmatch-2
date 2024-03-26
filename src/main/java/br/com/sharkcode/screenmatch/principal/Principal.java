package br.com.sharkcode.screenmatch.principal;

import br.com.sharkcode.screenmatch.models.DadosEpisodio;
import br.com.sharkcode.screenmatch.models.DadosSerie;
import br.com.sharkcode.screenmatch.models.DadosTemporada;
import br.com.sharkcode.screenmatch.models.Episodio;
import br.com.sharkcode.screenmatch.services.ConsumoApi;
import br.com.sharkcode.screenmatch.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=230c22ac";
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca:");

        var nomeSerie = leitura.nextLine();
        var json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY + "&season=" + i);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titutlo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t-> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("\n Top 5 episodios");
        dadosEpisodios.stream()
                .filter(e-> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t->t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);
    }
}
