package com.example.PharmaPhorm.funcionario;
import com.example.PharmaPhorm.Enum.Genero;
import com.example.PharmaPhorm.Enum.Setor;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Entity
public class Funcionario {
    private @Id @GeneratedValue Long id;

    private String nome;
    private int idade;
    private Genero genero;
    private Setor setor;
    private double salariobase;

    private double VA = 300;
    private double VR = 300;
    private double VT = 200;
    private double PLANO_SAUDE = 3000;
    private double PLANO_ODONTO = 3000;

    public Funcionario() {

    }

    Funcionario(String nome, int idade, String genero, String setor, double salariobase){
        this.nome = nome;
        this.idade = idade;
        this.genero = Genero.valueOf(genero);
        this.setor = Setor.valueOf(setor);
        this.salariobase = salariobase;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Funcionario that = (Funcionario) o;
        return idade == that.idade && Double.compare(salariobase, that.salariobase) == 0 && Double.compare(VA, that.VA) == 0 && Double.compare(VR, that.VR) == 0 && Double.compare(VT, that.VT) == 0 && Double.compare(PLANO_SAUDE, that.PLANO_SAUDE) == 0 && Double.compare(PLANO_ODONTO, that.PLANO_ODONTO) == 0 && Objects.equals(id, that.id) && Objects.equals(nome, that.nome) && genero == that.genero && setor == that.setor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, idade, genero, setor, salariobase, VA, VR, VT, PLANO_SAUDE, PLANO_ODONTO);
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "nome='" + nome + '\'' +
                ", idade=" + idade +
                ", genero=" + genero +
                ", setor=" + setor +
                ", salariobase=" + salariobase +
                ", id=" + id +
                ", VR=" + VR +
                ", VA=" + VA +
                ", VT=" + VT +
                ", PLANO_SAUDE=" + PLANO_SAUDE +
                ", PLANO_ODONTO=" + PLANO_ODONTO +
                '}';
    }
}
