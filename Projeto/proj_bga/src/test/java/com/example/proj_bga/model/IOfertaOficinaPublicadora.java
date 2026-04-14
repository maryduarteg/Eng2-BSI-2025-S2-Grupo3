package com.example.proj_bga.model;


// interface do sujeito
public interface IOfertaOficinaPublicadora {
    void adicionarObservador(IOfertaOficinaObserver observador);
    void removerObservador(IOfertaOficinaObserver observador);
    void notificarObservadores(OfertaOficina oferta, Oficina oficina);
}