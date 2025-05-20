package Model;

public class Categoria {
    private int id_categoria;
    private String nome;
    private String tamanho;
    private String embalagem;

    // Construtor vazio
    public Categoria() {

    }

    // Construtor com parametro
    public Categoria(int id_categoria, String nome, String tamanho, String embalagem) {
        this.id_categoria = id_categoria;
        this.nome = nome;
        this.tamanho = tamanho;
        this.embalagem = embalagem;
    }

    // Get e setters
    public int getId() {
        return id_categoria;
    }
    public void setId(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTamanho() {
        return tamanho;
    }
    public void setTamanho(String tamanho) {
        this.tamanho = tamanho;
    }

    public String getEmbalagem() {
        return embalagem;
    }
    public void setEmbalagem(String embalagem) {
        this.embalagem = embalagem;
    }
}
