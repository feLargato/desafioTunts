package com.desafio.model;


public class Student {

    private int matricula;
    private String aluno;
    private int faltas;
    private int p1;
    private int p2;
    private int p3;
    private int average;
    private String situacao;
    private int notaAprovacao;

    public Student(Object matricula, Object aluno, Object faltas, Object p1, Object p2, Object p3) {
        this.matricula = Integer.parseInt(matricula.toString());
        this.aluno = aluno.toString();
        this.faltas = Integer.parseInt(faltas.toString());
        this.p1 = Integer.parseInt(p1.toString());
        this.p2 = Integer.parseInt(p2.toString());
        this.p3 = Integer.parseInt(p3.toString());
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao() {
        String situacaoDefinition = "";
        this.setNotaAprovacao(0);

        if(faltas > 15) {
           situacaoDefinition = "Reprovado por Falta";
        }
        else if(average >= 70) {
            situacaoDefinition = "Aprovado";
        }
        else if(average < 50) {
            situacaoDefinition = "Reprovado";
        }
        else  {
            this.setNotaAprovacao((50 * 2) - average);
            situacaoDefinition = "Exame Final";
        }
        this.situacao = situacaoDefinition;
    }

    public int getNotaAprovacao() {
        return notaAprovacao;
    }

    public void setNotaAprovacao(int notaAprovacao) {
        this.notaAprovacao = notaAprovacao;
    }

    public void setAverage() {
        this.average = (p1 + p2 + p3) / 3;
    }


}
