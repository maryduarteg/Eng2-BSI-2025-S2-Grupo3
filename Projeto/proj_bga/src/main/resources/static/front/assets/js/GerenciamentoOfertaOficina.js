let todasOficinas = []; // armazenará a lista completa
let debounceTimer = null;

document.addEventListener('DOMContentLoaded', (event) => {
    const btnMostrar = document.getElementById('btn-mostrar-form');
    if (btnMostrar) {
        btnMostrar.addEventListener('click', toggleFormulario);
    }

    const inputDataOficina = document.getElementById('data-oficina');
    if (inputDataOficina) {
        inputDataOficina.addEventListener('blur', validarData);
    }
});



window.addEventListener("load", function() {
    document.querySelectorAll("form").forEach(form => form.reset());
});

function toggleFormulario() {
    const form = document.getElementById('formulario-cadastro');
    const botao = document.getElementById('btn-mostrar-form');

    if (form && botao) {
        form.classList.remove('d-none');
    } else {
        console.error("Erro: Um dos IDs do formulário ou botão não foi encontrado!.");
    }
}

function aplicarMascaraData(campo) {
    let valor = campo.value;

    valor = valor.replace(/\D/g, "");
    valor = valor.slice(0, 8);
    if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d)/, "$1/$2");
    }

    if (valor.length > 5) {
        valor = valor.replace(/^(\d{2})\/(\d{2})(\d)/, "$1/$2/$3");
    }
    campo.value = valor;
}

function aplicarMascaraHora(campo) {
    let valor = campo.value;
    valor = valor.replace(/\D/g, "");

    valor = valor.slice(0, 4);

    if (valor.length > 2) {
        valor = valor.replace(/^(\d{2})(\d)/, "$1:$2");
    }

    campo.value = valor;
}

function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById("formOficina");
    const campos = Array.from(form.querySelectorAll(".form-control"));

    // Remove borda vermelha e mensagem ao digitar
    campos.forEach(campo => {
        campo.addEventListener("input", () => {
            campo.classList.remove("is-invalid");
            const erroMsg = campo.nextElementSibling;
            if (erroMsg && erroMsg.classList.contains("invalid-feedback")) {
                erroMsg.remove();
            }
        });
    });

    // Submissão do formulário
    form.addEventListener("submit", cadastrarOficina);
});

// Máscaras para datas e horas
document.getElementById("data-inicio-oficina").addEventListener("keyup", e => aplicarMascaraData(e.target));
document.getElementById("data-fim-oficina").addEventListener("keyup", e => aplicarMascaraData(e.target));
document.getElementById("hora-inicio-oficina").addEventListener("keyup", e => aplicarMascaraHora(e.target));
document.getElementById("hora-fim-oficina").addEventListener("keyup", e => aplicarMascaraHora(e.target));


// Função para carregar professores nos selects de cadastro e edição
function carregarProfessores() {
    const selects = [
        document.getElementById("professor-select"),   // cadastro
        document.getElementById("editar-professor")    // edição
    ];

    fetch("http://localhost:8080/apis/ofertaOficina/professores")
        .then(resp => resp.json())
        .then(listaProfessores => {
            selects.forEach(select => {
                if (!select) return;
                // Limpa opções antigas e adiciona placeholder
                select.innerHTML = '';

                listaProfessores.forEach(prof => {
                    const option = document.createElement("option");
                    option.value = prof.id;      // id do professor vindo do backend
                    option.textContent = prof.nome; // nome do professor vindo do backend
                    select.appendChild(option);
                });
            });
        })
        .catch(err => console.error("Erro ao carregar professores:", err));
}

// Chamar função após DOM carregado
document.addEventListener('DOMContentLoaded', () => {
    carregarProfessores();
    carregarOficinasTabelaSuper(); //para as oficinas mães da tabela atual
});

function carregarOficinasTabelaSuper()
{
    const selects = [
        document.getElementById("nome-select"),   // cadastro
        document.getElementById("editar-nome")    // edição
    ];

    fetch("http://localhost:8080/apis/oficina")
        .then(resp => resp.json())
        .then(listaOficinas => {
            selects.forEach(select => {
                if (!select) return;
                // Limpa opções antigas e adiciona placeholder
                select.innerHTML = '';

                listaOficinas.forEach(ofc => {
                    if(ofc.ativo != 'N')
                    {
                        const option = document.createElement("option");
                        option.value = ofc.idOficina;      // id do professor vindo do backend
                        option.textContent = ofc.descricao; // nome do professor vindo do backend
                        select.appendChild(option);
                    }
                });
            });
        })
        .catch(err => console.error("Erro ao carregar oficinas:", err));
}


function cadastrarOficina(event) {
    event.preventDefault();
    const form = event.target;
    const campos = Array.from(form.querySelectorAll(".form-control"));
    let valido = true;

    // Limpa erros
    campos.forEach(campo => {
        campo.classList.remove("is-invalid");
        const erroMsg = campo.nextElementSibling;
        if (erroMsg && erroMsg.classList.contains("invalid-feedback")) erroMsg.remove();
    });

    // Campos obrigatórios
    campos.forEach(campo => {
        if (!campo.value.trim()) {
            adicionarErro(campo, "Campo obrigatório");
            valido = false;
        }
    });

    // Validação professor
    const professorId = parseInt(form.professorId.value);
    if (professorId < 1 || isNaN(professorId)) {
        adicionarErro(form.professorId, "ID do professor inválido");
        valido = false;
    }

    // Validação de data (formato DD/MM/AAAA)
    const dataInicio = form.dataInicio.value.trim();
    const dataFim = form.dataFim.value.trim();
    if (dataInicio && dataFim) {
        const [di, mi, ai] = dataInicio.split("/").map(Number);
        const [df, mf, af] = dataFim.split("/").map(Number);

        const hoje = new Date();
        hoje.setHours(0, 0, 0, 0);
        const inicio = new Date(ai, mi - 1, di);
        const fim = new Date(af, mf - 1, df);

        if (inicio < hoje) {
            adicionarErro(form.dataInicio, "A data inicial não pode ser anterior à data atual");
            valido = false;
        }
        if (fim < inicio) {
            adicionarErro(form.dataFim, "A data final deve ser igual ou posterior à data inicial");
            valido = false;
        }
    }

    // Validação de hora (HH:MM)
    const horaRegex = /^([01]\d|2[0-3]):([0-5]\d)$/;
    const horaInicio = form.horaInicio.value.trim();
    const horaFim = form.horaFim.value.trim();

    if (!horaRegex.test(horaInicio)) {
        adicionarErro(form.horaInicio, "Hora inválida (use formato 24h: HH:MM)");
        valido = false;
    }
    if (!horaRegex.test(horaFim)) {
        adicionarErro(form.horaFim, "Hora inválida (use formato 24h: HH:MM)");
        valido = false;
    }

    if (horaRegex.test(horaInicio) && horaRegex.test(horaFim)) {
        const [hi, mi] = horaInicio.split(":").map(Number);
        const [hf, mf] = horaFim.split(":").map(Number);
        const inicio = new Date();
        const fim = new Date();
        inicio.setHours(hi, mi, 0, 0);
        fim.setHours(hf, mf, 0, 0);
        if (fim <= inicio) {
            adicionarErro(form.horaFim, "Hora final deve ser posterior à hora inicial");
            valido = false;
        }
    }

    if (!valido) return;

    // Monta objeto para envio
    const oficina = {
        ofc_fk: parseInt(form.nome.value),
        professor: professorId,
        dataInicio: converterDataBrasilParaISO(form.dataInicio.value),
        dataFim: converterDataBrasilParaISO(form.dataFim.value),
        horaInicio: form.horaInicio.value,
        horaTermino: form.horaFim.value,
        ativo: "S"
    };


    // Verifica conflito de horários no backend antes de cadastrar
    fetch(`http://localhost:8080/apis/ofertaOficina/verificar-conflito?professorId=${professorId}&dataInicio=${oficina.dataInicio}&dataFim=${oficina.dataFim}&horaInicio=${oficina.horaInicio}&horaFim=${oficina.horaTermino}`)
        .then(resp => resp.json())
        .then(conflito => {
            if (conflito.existe) {
                adicionarErro(form.professorId, "O professor já possui oficina nesse horário!");
                mostrarMensagem("Conflito detectado: professor já ocupado nesse horário.", false);
            } else {
                // Se não há conflito, cadastrar

                fetch("http://localhost:8080/apis/ofertaOficina", {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(oficina)
                })
                    .then(resp => {
                        if (!resp.ok) throw new Error("Erro ao cadastrar");
                        return resp.json();
                    })
                    .then(() => {
                        mostrarMensagem("Sucesso! Oficina cadastrada.", true);
                        form.reset();
                    })
                    .catch(() => {
                        mostrarMensagem("Erro ao cadastrar oficina!", false);
                    });
            }
        })
        .catch(() => mostrarMensagem("Erro ao verificar conflito!", false));

    // Função auxiliar para erro
    function adicionarErro(campo, msg) {
        campo.classList.add("is-invalid");
        if (!campo.nextElementSibling || !campo.nextElementSibling.classList.contains("invalid-feedback")) {
            const div = document.createElement("div");
            div.className = "invalid-feedback";
            div.textContent = msg;
            campo.after(div);
        }
    }
}

// Converte DD/MM/AAAA → YYYY-MM-DD
function converterDataBrasilParaISO(data) {
    const [dia, mes, ano] = data.split("/");
    return `${ano}-${mes}-${dia}`;
}


function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}

function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-oficina");
    if (container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
}



function formatarDataParaBR(dataISO) {
    if (!dataISO) return "";
    const data = new Date(dataISO);
    if (isNaN(data)) return "";
    const dia = String(data.getDate()).padStart(2, "0");
    const mes = String(data.getMonth() + 1).padStart(2, "0");
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

document.addEventListener('DOMContentLoaded', () => {
    carregarTodasOficinas();

    // Botão de salvar edição
    const btnSalvar = document.getElementById("btn-salvar-edicao");
    if (btnSalvar) btnSalvar.addEventListener("click", salvarEdicao);
});

// Função para carregar todas as oficinas
function carregarTodasOficinas() {
    fetch("http://localhost:8080/apis/ofertaOficina")
        .then(resp => resp.json())
        .then(data => {
            todasOficinas = data; // ← salva a lista completa
            aplicarFiltros();     // ← atualiza a tabela com filtros
            document.getElementById("tabela-container").classList.remove("d-none");
        })
        .catch(err => console.error(err));
}

function aplicarFiltros() {
    const nomeFiltro = document.getElementById("filtro-nome").value.toLowerCase();
    const statusFiltro = document.getElementById("filtro-status").value; // S, N ou vazio

    const tbody = document.getElementById("tabela-oficina-container");
    const msgContainer = document.getElementById("mensagem-tabela");
    tbody.innerHTML = '';

    let lista = todasOficinas;

    // Filtro por nome
    if (nomeFiltro) {
        lista = lista.filter(o => o.nome && o.nome.toLowerCase().includes(nomeFiltro));
    }

    // Filtro por ativo / inativo
    if (statusFiltro !== "") {
        lista = lista.filter(o => o.ativo === statusFiltro);
    }

    // Se não encontrar nada
    if (lista.length === 0) {
        msgContainer.textContent = "Nenhuma oficina encontrada com os filtros selecionados.";
        return;
    } else {
        msgContainer.textContent = "";
    }

    // Monta tabela filtrada
    lista.forEach(oficina => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${oficina.idOficina ?? "-"}</td>
            <td>${oficina.nome ?? "-"}</td>
            <td>${formatarDataParaBR(oficina.data_inicio) ?? "-"}</td>
            <td>${formatarDataParaBR(oficina.data_fim) ?? "-"}</td>
            <td>${oficina.hora_inicio ? oficina.hora_inicio.slice(0,5) : "-"}</td>
            <td>${oficina.hora_termino ? oficina.hora_termino.slice(0,5) : "-"}</td>
            <td>${oficina.pde_id ?? "-"}</td>
            <td>${oficina.ativo === "S" ? "Ativa" : "Inativa"}</td>
            <td><button class="btn btn-sm btn-outline-info btn-editar" data-id="${oficina.idOficina}"><i class="fas fa-edit"></i></button></td>
            <td><button class="btn btn-sm btn-danger btn-excluir" data-id="${oficina.idOficina}"><i class="fas fa-trash"></i></button></td>
        `;
    });

    ativarBotoesExcluir();
    ativarBotoesEditar();
}



// Inativar oficina
function ativarBotoesExcluir() {
    document.querySelectorAll(".btn-excluir").forEach(botao => {
        botao.addEventListener("click", () => {
            const id = botao.dataset.id;
            if (!id || !confirm("Deseja realmente inativar esta oficina?")) return;

            fetch(`http://localhost:8080/apis/ofertaOficina/inativar/${id}`, { method: "PUT" })
                .then(resp => {
                    if (!resp.ok) throw new Error("Erro ao inativar oficina");
                    botao.closest("tr").cells[7].textContent = "Inativa";
                    mostrarMensagem("Oficina inativada com sucesso!", true);
                })
                .catch(err => {
                    console.error(err);
                    mostrarMensagem("Erro ao inativar oficina.", false);
                });
        });
    });
}

// Abrir formulário de edição
function ativarBotoesEditar() {
    document.querySelectorAll(".btn-editar").forEach(botao => {
        botao.addEventListener("click", () => {
            const id = botao.dataset.id;
            fetch(`http://localhost:8080/apis/ofertaOficina/${id}`)
                .then(resp => resp.json())
                .then(oficina => {
                    console.log(oficina);
                    document.getElementById("editar-id").value = oficina.idOficina;
                    document.getElementById("editar-nome").value = oficina.ofc_fk;
                    document.getElementById("editar-data-inicio").value = formatarDataParaBR(oficina.data_inicio);
                    document.getElementById("editar-data-fim").value = formatarDataParaBR(oficina.data_fim);
                    document.getElementById("editar-hora-inicio").value = oficina.hora_inicio?.slice(0,5) || "";
                    document.getElementById("editar-hora-fim").value = oficina.hora_termino?.slice(0,5) || "";
                    document.getElementById("editar-professor").value = oficina.pde_id;
                    document.getElementById("editar-ativo").value = oficina.ativo;

                    document.getElementById("formulario-editar-oficina").classList.remove("d-none");

                    //Máscaras aplicadas ao abrir o formulário de edição
                    document.getElementById("editar-data-inicio").addEventListener("keyup", e => aplicarMascaraData(e.target));
                    document.getElementById("editar-data-fim").addEventListener("keyup", e => aplicarMascaraData(e.target));
                    document.getElementById("editar-hora-inicio").addEventListener("keyup", e => aplicarMascaraHora(e.target));
                    document.getElementById("editar-hora-fim").addEventListener("keyup", e => aplicarMascaraHora(e.target));
                });
        });
    });
}


// Salvar edição
function salvarEdicao() {
    const form = document.getElementById("form-editar-oficina");
    let valido = true;

    const id = document.getElementById("editar-id").value;
    const nome = document.getElementById("editar-nome").value;
    const dataInicioCampo = document.getElementById("editar-data-inicio");
    const dataFimCampo = document.getElementById("editar-data-fim");
    const horaInicioCampo = document.getElementById("editar-hora-inicio");
    const horaFimCampo = document.getElementById("editar-hora-fim");
    const professor = document.getElementById("editar-professor");
    const ativo = document.getElementById("editar-ativo").value;

    const campos = [dataInicioCampo, dataFimCampo, horaInicioCampo, horaFimCampo, professor];

    // Remove erros existentes
    campos.forEach(campo => {
        campo.classList.remove("is-invalid");
        if (campo.nextElementSibling && campo.nextElementSibling.classList.contains("invalid-feedback")) {
            campo.nextElementSibling.remove();
        }
    });

    function erro(campo, msg) {
        campo.classList.add("is-invalid");
        const div = document.createElement("div");
        div.className = "invalid-feedback";
        div.textContent = msg;
        campo.after(div);
        valido = false;
    }

    // Validação campos vazios
    campos.forEach(campo => {
        if (!campo.value.trim()) erro(campo, "Campo obrigatório");
    });

    const dataInicio = dataInicioCampo.value.trim();
    const dataFim = dataFimCampo.value.trim();
    const horaInicio = horaInicioCampo.value.trim();
    const horaFim = horaFimCampo.value.trim();
    const professorId = parseInt(professor.value);

    // Validação de datas
    if (dataInicio && dataFim) {
        const hoje = new Date(); hoje.setHours(0,0,0,0);
        const [di, mi, ai] = dataInicio.split("/").map(Number);
        const [df, mf, af] = dataFim.split("/").map(Number);
        const inicio = new Date(ai, mi - 1, di);
        const fim = new Date(af, mf - 1, df);

        if (inicio < hoje) erro(dataInicioCampo, "A data inicial não pode ser anterior à data atual");
        if (fim < inicio) erro(dataFimCampo, "A data final deve ser igual ou posterior à inicial");
    }

    // Validação de horas (formato + ordem)
    const horaRegex = /^([01]\d|2[0-3]):([0-5]\d)$/;
    if (!horaRegex.test(horaInicio)) erro(horaInicioCampo, "Use o formato 24h (HH:MM)");
    if (!horaRegex.test(horaFim)) erro(horaFimCampo, "Use o formato 24h (HH:MM)");

    if (horaRegex.test(horaInicio) && horaRegex.test(horaFim)) {
        const [hi, mi] = horaInicio.split(":").map(Number);
        const [hf, mf] = horaFim.split(":").map(Number);
        const inicioHora = hi * 60 + mi;
        const fimHora = hf * 60 + mf;
        if (fimHora <= inicioHora) erro(horaFimCampo, "Hora final deve ser maior que a inicial");
    }

    if (!valido) return;

    //Verificação de conflito antes de atualizar (mesmo do cadastrar)
    fetch(`http://localhost:8080/apis/ofertaOficina/verificar-conflito?professorId=${professorId}&dataInicio=${converterDataBrasilParaISO(dataInicio)}&dataFim=${converterDataBrasilParaISO(dataFim)}&horaInicio=${horaInicio}&horaFim=${horaFim}&ignorarId=${id}`)
        .then(resp => resp.json())
        .then(conflito => {
            if (conflito.existe) {
                erro(professor, "O professor já possui oficina nesse horário!");
                mostrarMensagem("Conflito detectado: professor já está ocupado.", false);
                return;
            }

            // Se não há conflito → atualizar
            const params = new URLSearchParams({
                id: id,
                Hora_Inicio: horaInicio,
                Hora_Fim: horaFim,
                Data_Inicio: converterDataBrasilParaISO(dataInicio),
                Data_Fim: converterDataBrasilParaISO(dataFim),
                Professor: professorId,
                Ativo: ativo,
                ofc_pk: parseInt(nome)
            });

            fetch(`http://localhost:8080/apis/ofertaOficina?${params.toString()}`, { method: "PUT" })
                .then(resp => {
                    if (!resp.ok) throw new Error();
                    mostrarMensagem("Alteração realizada com sucesso!", true);
                    document.getElementById("formulario-editar-oficina").classList.add("d-none");
                    carregarTodasOficinas();
                })
                .catch(() => mostrarMensagem("Erro ao atualizar oficina!", false));
        });
}

// Funções auxiliares
function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-oficina");
    if (container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
}

function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}



// BUSCA DE OFICINA POR ID
document.addEventListener('DOMContentLoaded', () => {
    const btnBuscar = document.getElementById('btn-buscar-oficina');
    const btnBuscarNome = document.getElementById("btn-buscar-nome");
    const btnLimpar = document.getElementById('btn-limpar-busca');
    const inputBusca = document.getElementById('input-busca-id');
    const tabela = document.getElementById('tabela-oficina-container');
    const tabelaContainer = document.getElementById('tabela-container');
    const mensagemTabela = document.getElementById('mensagem-tabela');
    const inputNomeBusca = document.getElementById("filtro-nome");

    btnBuscar.addEventListener('click', () => {
        const id = inputBusca.value.trim();
        if (!id) {
            alert('Digite um ID válido para buscar.');
            return;
        }

        // Faz a requisição para buscar a oficina por ID
        fetch(`http://localhost:8080/apis/ofertaOficina/${id}`) // ajuste a URL conforme sua API
            .then(response => {
                if (!response.ok) throw new Error('Oficina não encontrada');
                return response.json();
            })
            .then(oficina => {
                tabelaContainer.classList.remove('d-none');
                tabela.innerHTML = `
                    <tr>
                        <td>${oficina.idOficina}</td>
                        <td>${oficina.nome}</td>
                        <td>${oficina.data_inicio}</td>
                        <td>${oficina.data_fim}</td>
                        <td>${oficina.hora_inicio}</td>
                        <td>${oficina.hora_termino}</td>
                        <td>${oficina.pde_id}</td>
                        <td>${oficina.ativo}</td>
                                            <td>
                        <button class="btn btn-sm btn-outline-info btn-editar" data-id="${oficina.idOficina}" title="Editar Oficina">
                               <i class="fas fa-edit"></i>
                        </button>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-danger btn-excluir" data-id="${oficina.idOficina}" title="Inativar Oficina">
                                <i class="fas fa-trash"></i>
                            </button>
                        </td>
                    </tr>
                `;
                mensagemTabela.textContent = '';
                ativarBotoesExcluir();
                ativarBotoesEditar();
            })
            .catch(err => {
                tabela.innerHTML = '';
                mensagemTabela.textContent = 'Oficina não encontrada.';
            });
    });

    btnLimpar.addEventListener('click', () => {
        inputBusca.value = '';
        tabela.innerHTML = '';
        mensagemTabela.textContent = 'Carregando oficinas...';
        tabelaContainer.classList.add('d-none');
        carregarTodasOficinas(); // chama sua função padrão para listar todas
    });

    //Buscar oficina por contém parte da "palavra
    btnBuscarNome.addEventListener('click', () => {

        const ctd = inputNomeBusca.value.trim();
        if (!ctd) {
            alert('Informe o que deseja buscar.');
            return;
        }

        fetch(`http://localhost:8080/apis/ofertaOficina`) // ajuste a URL conforme sua API
            .then(response => {
                if (!response.ok) throw new Error('Oficina não encontrada');
                return response.json();
            })
            .then(listaOficina => {
                tabelaContainer.classList.remove('d-none');
                listaOficina.forEach(ofc => {
                    let nome = ofc.nome;
                    if(nome.includes(ctd))
                    {
                        tabela.innerHTML += `
                            <tr>
                                <td>${oficina.idOficina}</td>
                                <td>${oficina.nome}</td>
                                <td>${oficina.data_inicio}</td>
                                <td>${oficina.data_fim}</td>
                                <td>${oficina.hora_inicio}</td>
                                <td>${oficina.hora_termino}</td>
                                <td>${oficina.pde_id}</td>
                                <td>${oficina.ativo}</td>
                                                    <td>
                                <button class="btn btn-sm btn-outline-info btn-editar" data-id="${oficina.idOficina}" title="Editar Oficina">
                                       <i class="fas fa-edit"></i>
                                </button>
                                </td>
                                <td>
                                    <button class="btn btn-sm btn-danger btn-excluir" data-id="${oficina.idOficina}" title="Inativar Oficina">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </tr>
                        `;
                    }
                });
                mensagemTabela.textContent = '';
                ativarBotoesExcluir();
                ativarBotoesEditar();
            })
            .catch(err => {
                tabela.innerHTML = '';
                mensagemTabela.textContent = 'Oficina não encontrada.';
            });

    });
});


document.addEventListener("DOMContentLoaded", () => {
    const filtroStatus = document.getElementById("filtro-status");
    const filtroNome = document.getElementById("filtro-nome");

    if (filtroStatus) filtroStatus.addEventListener("change", aplicarFiltros);
    if (filtroNome) filtroNome.addEventListener("keyup", aplicarFiltros);
});
