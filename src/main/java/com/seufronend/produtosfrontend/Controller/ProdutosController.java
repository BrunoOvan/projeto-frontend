package com.seufronend.produtosfrontend.Controller;

import com.seufronend.produtosfrontend.Produto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    private final List<Produto> produtos = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong proximoId = new AtomicLong(1);

    @GetMapping
    public List<Produto> listarProdutos() {
        return produtos;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProduto(@PathVariable Long id) {
        Optional<Produto> produto = produtos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();

        return produto
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody Produto produto) {
        if (produto.getNome() == null || produto.getNome().isBlank() || produto.getPreco() == null) {
            return ResponseEntity.badRequest().build();
        }

        produto.setId(proximoId.getAndIncrement());
        produtos.add(produto);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable Long id, @RequestBody Produto dados) {
        synchronized (produtos) {
            Optional<Produto> produtoExistente = produtos.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst();

            if (produtoExistente.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Produto produto = produtoExistente.get();
            produto.setNome(dados.getNome());
            produto.setPreco(dados.getPreco());
            return ResponseEntity.ok(produto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirProduto(@PathVariable Long id) {
        boolean removido;
        synchronized (produtos) {
            removido = produtos.removeIf(produto -> produto.getId().equals(id));
        }

        return removido ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
