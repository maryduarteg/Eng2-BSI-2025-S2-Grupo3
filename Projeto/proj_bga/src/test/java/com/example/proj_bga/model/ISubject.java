package com.example.proj_bga.model;


// interface do sujeito
public interface ISubject {
    void adicionarObservador(IObserver observador);
    void removerObservador(IObserver observador);
    void notificarObservadores(OfertaOficina oferta, Oficina oficina);
}