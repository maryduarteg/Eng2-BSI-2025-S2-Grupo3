package com.example.proj_bga.controller;

import com.example.proj_bga.model.Pessoa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PessoaController {
    @Autowired
    private Pessoa pessoaModel;
}
