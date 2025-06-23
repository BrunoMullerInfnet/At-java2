package org.example;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class App {
    private static List<Aluno> alunos = new ArrayList<>();

    public static void main(String[] args) {
        ObjectMapper objectMapper = new ObjectMapper();

        Javalin app = Javalin.create(config -> {
            config.jsonMapper(new JavalinJackson(objectMapper));
        }).start(7000);

        app.get("/hello", ctx -> {
            ctx.result("Hello, Javalin!");
        });

        app.get("/status", ctx ->{
            ctx.json(Map.of("status" , "ok","timestamp", Instant.now().toString()
            ));
        });

        app.post("/echo",ctx ->{
            Map<String,Object> corpoJson = ctx.bodyAsClass(Map.class);
            ctx.json(corpoJson);
        });

        app.get("/saudacao/{nome}" , ctx ->{
            String nome = ctx.pathParam("nome");
            ctx.json(Map.of("mensagem", "Olá " + nome));
        });

        app.post("/alunos", ctx ->{
            Aluno novoAluno = ctx.bodyAsClass(Aluno.class);
            alunos.add(novoAluno);
            ctx.status(201);
            ctx.json(novoAluno);
        });

        app.get("/alunos", ctx ->{
            ctx.json(alunos);
        });

        app.get("/alunos/{id}", ctx ->{
            String idBusca = ctx.pathParam("id");

            Optional<Aluno> alunoRetornado = alunos.stream()
                    .filter(aluno -> aluno.getId().equalsIgnoreCase(idBusca))
                    .findFirst();

            if(alunoRetornado.isPresent()){
                ctx.json(alunoRetornado.get());
            } else{
                ctx.status(404);
                ctx.json(Map.of("erro","Aluno não encontrado"));
            }
        });

    }
}
