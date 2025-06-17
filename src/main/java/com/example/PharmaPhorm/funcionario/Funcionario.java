package com.example.PharmaPhorm.funcionario;
import com.example.PharmaPhorm.Enum.Genero;
import com.example.PharmaPhorm.Enum.Setor;
import com.example.PharmaPhorm.negocio.Negocio;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Funcionario {
    private @Id @GeneratedValue Long id;

    private String nome;
    private int idade;
    private Genero genero;
    private Setor setor;
    private double salariobase;
    private double salarioLiquido;

    private double VA = 300;
    private double VR = 300;
    private double VT = 200;
    private double PLANO_SAUDE = 3000;
    private double PLANO_ODONTO = 3000;

    @ManyToMany
    @JoinTable(
            name = "negocio_funcionario",
            joinColumns = @JoinColumn(name = "funcionario_id"),
            inverseJoinColumns = @JoinColumn(name = "negocio_id")
    )
    @JsonIgnore // Importante para evitar loop infinito na serialização JSON
    private Set<Negocio> negociosParticipantes;

    public Funcionario() {
        this.negociosParticipantes = new HashSet<>();
    }


    public Funcionario(String nome, int idade, String genero, String setor, double salariobase){
        this.nome = nome;
        this.idade = idade;
        this.genero = Genero.valueOf(genero.toUpperCase());
        this.setor = Setor.valueOf(setor.toUpperCase());
        this.salariobase = salariobase;
        this.negociosParticipantes = new HashSet<>();
        this.salarioLiquido = this.salariobase - calcularValorIR();
        ajustarBeneficiosPorSetor();
    }

    // ✅ MÉTODO GETTER ADICIONADO AQUI
    public Set<Negocio> getNegociosParticipantes() {
        return negociosParticipantes;
    }

    public void setNegociosParticipantes(Set<Negocio> negociosParticipantes) {
        this.negociosParticipantes = negociosParticipantes;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    public double getSalariobase() {
        return salariobase;
    }

    public void setSalariobase(double salariobase) {
        this.salariobase = salariobase;
    }

    public double getVA() {
        return VA;
    }

    public void setVA(double VA) {
        this.VA = VA;
    }

    public double getVR() {
        return VR;
    }

    public void setVR(double VR) {
        this.VR = VR;
    }

    public double getVT() {
        return VT;
    }

    public void setVT(double VT) {
        this.VT = VT;
    }

    public double getPLANO_SAUDE() {
        return PLANO_SAUDE;
    }

    public void setPLANO_SAUDE(double PLANO_SAUDE) {
        this.PLANO_SAUDE = PLANO_SAUDE;
    }

    public double getPLANO_ODONTO() {
        return PLANO_ODONTO;
    }

    public void setPLANO_ODONTO(double PLANO_ODONTO) {
        this.PLANO_ODONTO = PLANO_ODONTO;
    }

    // Métodos de Lógica de Negócio
    private void ajustarBeneficiosPorSetor() {
        switch (this.setor) {
            case ATENDIMENTO_AO_CLIENTE:
            case VENDAS:
                this.PLANO_ODONTO -= 1000;
                break;
            case FINANCEIRO:
            case ALMOXARIFADO:
            case GESTAO_DE_PESSOAS:
                this.VA += 200;
                this.VR += 200;
                this.PLANO_SAUDE += 500;
                this.PLANO_ODONTO -= 500;
                break;
            case GERENCIA:
                this.VA += 700;
                this.VR += 700;
                this.PLANO_SAUDE += 1200;
                break;
            default:
                // Nenhum benefício adicional para outros setores
                break;
        }
    }

    public double calcularValorIR() {
        if (salariobase <= 2428.80) return 0.0;
        if (salariobase <= 2826.65) return (salariobase * 0.075) - 182.16;
        if (salariobase <= 3751.05) return (salariobase * 0.15) - 394.16;
        if (salariobase <= 4664.68) return (salariobase * 0.225) - 675.49;
        return (salariobase * 0.275) - 908.75;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                '}';
    }
}