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

function validarDataInicio() {
    const campo = document.getElementById("data-inicio-oficina");
    const dataBr = campo.value.trim();
    let mensagemErro = "";

    if (dataBr === "") {
        campo.setCustomValidity("");
        return true;
    }

    if (dataBr.length != 10 || dataBr.indexOf('/') == -1)
        mensagemErro = "Formato de data incompleto. Use DD/MM/AAAA.";
    else {
        const partes = dataBr.split("/");
        const dia = parseInt(partes[0], 10);
        const mes = parseInt(partes[1], 10);
        const ano = parseInt(partes[2], 10);
        const dataInseridaObj = new Date(ano, mes - 1, dia);
        const dataAtual = new Date();

        if (dataInseridaObj.getFullYear() != ano || (dataInseridaObj.getMonth() + 1) != mes || dataInseridaObj.getDate() != dia)
            mensagemErro = "A data inserida não é válida!";
        else if (dataInseridaObj.getTime() <= dataAtual.getTime())
            mensagemErro = "Não é possível agendar uma data anterior ou igual à data atual.";
    }
}

function validarDataFim() {
    const campoInicio = document.getElementById("data-inicio-oficina");
    const campoFim = document.getElementById("data-fim-oficina");
    const dataInicioBr = campoInicio.value.trim();
    const dataFimBr = campoFim.value.trim();
    let mensagemErro = "";

    if (dataFimBr === "") {
        campoFim.setCustomValidity("");
        return true;
    }

    if (dataFimBr.length != 10 || dataFimBr.indexOf('/') == -1) {
        mensagemErro = "Formato de data incompleto. Use DD/MM/AAAA.";
    } else {
        const partesInicio = dataInicioBr.split("/");
        const partesFim = dataFimBr.split("/");

        // Converter para objeto Date
        const dataInicio = new Date(partesInicio[2], partesInicio[1] - 1, partesInicio[0]);
        const dataFim = new Date(partesFim[2], partesFim[1] - 1, partesFim[0]);

        // Verificações básicas de validade
        if (isNaN(dataFim.getTime())) {
            mensagemErro = "A data final não é válida!";
        } else if (dataInicioBr !== "" && dataFim.getTime() <= dataInicio.getTime()) {
            mensagemErro = "A data final deve ser posterior à data inicial.";
        }
    }

    campoFim.setCustomValidity(mensagemErro);
    campoFim.reportValidity(); // mostra a mensagem
    return mensagemErro === "";
}

function validarHoraFim() {
    const campoInicio = document.getElementById("hora-inicio-oficina");
    const campoFim = document.getElementById("hora-fim-oficina");
    const horaInicioStr = campoInicio.value.trim();
    const horaFimStr = campoFim.value.trim();
    let mensagemErro = "";

    // Se o campo estiver vazio, não mostra erro ainda
    if (horaFimStr === "") {
        campoFim.setCustomValidity("");
        return true;
    }

    // Verifica formato básico HH:MM
    const regexHora = /^([01]\d|2[0-3]):([0-5]\d)$/;
    if (!regexHora.test(horaInicioStr) || !regexHora.test(horaFimStr)) {
        mensagemErro = "Formato de hora inválido. Use HH:MM.";
    } else {
        // Converte para objeto Date para comparar
        const [horaIni, minIni] = horaInicioStr.split(":").map(Number);
        const [horaFim, minFim] = horaFimStr.split(":").map(Number);

        const inicio = new Date();
        const fim = new Date();
        inicio.setHours(horaIni, minIni, 0, 0);
        fim.setHours(horaFim, minFim, 0, 0);

        if (fim.getTime() <= inicio.getTime()) {
            mensagemErro = "A hora de término deve ser posterior à hora de início.";
        }
    }

    campoFim.setCustomValidity(mensagemErro);
    campoFim.reportValidity();
    return mensagemErro === "";
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
document.getElementById("data-inicio-oficina").addEventListener("keyup", e => e.target.value = aplicarMascaraData(e.target.value));
document.getElementById("data-fim-oficina").addEventListener("keyup", e => e.target.value = aplicarMascaraData(e.target.value));
document.getElementById("hora-inicio-oficina").addEventListener("keyup", e => e.target.value = aplicarMascaraHora(e.target.value));
document.getElementById("hora-fim-oficina").addEventListener("keyup", e => e.target.value = aplicarMascaraHora(e.target.value));

function aplicarMascaraData(valor) {
    valor = valor.replace(/\D/g, "").slice(0,8);
    if (valor.length > 2) valor = valor.replace(/^(\d{2})(\d)/, "$1/$2");
    if (valor.length > 5) valor = valor.replace(/^(\d{2})\/(\d{2})(\d)/, "$1/$2/$3");
    return valor;
}

function aplicarMascaraHora(valor) {
    valor = valor.replace(/\D/g, "").slice(0,4);
    if (valor.length > 2) valor = valor.replace(/^(\d{2})(\d)/, "$1:$2");
    return valor;
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

    // Validação datas
    const dataInicio = form.dataInicio.value.trim();
    const dataFim = form.dataFim.value.trim();
    if (dataInicio && dataFim) {
        const [di, mi, ai] = dataInicio.split("/").map(Number);
        const [df, mf, af] = dataFim.split("/").map(Number);
        const inicio = new Date(ai, mi-1, di);
        const fim = new Date(af, mf-1, df);
        if (fim < inicio) {
            adicionarErro(form.dataFim, "Data final deve ser igual ou posterior à data inicial");
            valido = false;
        }
    }

    // Validação horas
    const horaInicio = form.horaInicio.value.trim();
    const horaFim = form.horaFim.value.trim();
    if (horaInicio && horaFim) {
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

    // Monta objeto
    const oficina = {
        nome: form.nome.value.trim(),
        professor: professorId,
        dataInicio: converterDataBrasilParaISO(form.dataInicio.value),
        dataFim: converterDataBrasilParaISO(form.dataFim.value),
        horaInicio: form.horaInicio.value,
        horaTermino: form.horaFim.value,
        ativo: "S"
    };

    fetch("http://localhost:8080/apis/oficina", {
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
            adicionarErro(form.professorId, "ID do professor inválido");
        });

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
    const tbody = document.getElementById("tabela-oficina-container");
    const msgContainer = document.getElementById("mensagem-tabela");
    const tabelaContainer = document.getElementById("tabela-container");

    if (!tbody || !msgContainer || !tabelaContainer) return;

    tabelaContainer.classList.remove("d-none");
    tbody.innerHTML = '';
    msgContainer.textContent = "Carregando todas as oficinas...";

    fetch("http://localhost:8080/apis/oficina")
        .then(resp => resp.json())
        .then(listaOficina => {
            tbody.innerHTML = '';
            if (!listaOficina || listaOficina.length === 0) {
                msgContainer.textContent = "Nenhuma oficina cadastrada.";
                return;
            }
            msgContainer.textContent = '';
            listaOficina.forEach(oficina => {
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
                `;
            });
            ativarBotoesExcluir();
            ativarBotoesEditar();
        })
        .catch(err => console.error(err));
}

// Inativar oficina
function ativarBotoesExcluir() {
    document.querySelectorAll(".btn-excluir").forEach(botao => {
        botao.addEventListener("click", () => {
            const id = botao.dataset.id;
            if (!id || !confirm("Deseja realmente inativar esta oficina?")) return;

            fetch(`http://localhost:8080/apis/oficina/inativar/${id}`, { method: "PUT" })
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
            fetch(`http://localhost:8080/apis/oficina/${id}`)
                .then(resp => resp.json())
                .then(oficina => {
                    document.getElementById("editar-id").value = oficina.idOficina;
                    document.getElementById("editar-nome").value = oficina.nome;
                    document.getElementById("editar-data-inicio").value = formatarDataParaBR(oficina.data_inicio);
                    document.getElementById("editar-data-fim").value = formatarDataParaBR(oficina.data_fim);
                    document.getElementById("editar-hora-inicio").value = oficina.hora_inicio?.slice(0,5) || "";
                    document.getElementById("editar-hora-fim").value = oficina.hora_termino?.slice(0,5) || "";
                    document.getElementById("editar-professor").value = oficina.pde_id;
                    document.getElementById("editar-ativo").value = oficina.ativo;

                    document.getElementById("formulario-editar-oficina").classList.remove("d-none");
                });
        });
    });
}

// Salvar edição
function salvarEdicao() {
    const id = document.getElementById("editar-id").value;
    const nome = document.getElementById("editar-nome").value.trim();
    const dataInicio = document.getElementById("editar-data-inicio").value.trim();
    const dataFim = document.getElementById("editar-data-fim").value.trim();
    const horaInicio = document.getElementById("editar-hora-inicio").value.trim();
    const horaFim = document.getElementById("editar-hora-fim").value.trim();
    const professor = document.getElementById("editar-professor").value;
    const ativo = document.getElementById("editar-ativo").value;

    if (!id || !nome || !dataInicio || !dataFim || !horaInicio || !horaFim || !professor) {
        alert("Preencha todos os campos!");
        return;
    }

    // Monta query params para o backend
    const params = new URLSearchParams({
        id: id,
        Nome: nome,
        Hora_Inicio: horaInicio,
        Hora_Fim: horaFim,
        Data_Inicio: converterDataBrasilParaISO(dataInicio),
        Data_Fim: converterDataBrasilParaISO(dataFim),
        Professor: professor,
        Ativo: ativo
    });

    fetch(`http://localhost:8080/apis/oficina?${params.toString()}`, { method: "PUT" })
        .then(resp => {
            if (!resp.ok) throw new Error("Erro ao atualizar oficina");
            return resp.json();
        })
        .then(() => {
            mostrarMensagem("Alteração concluída com sucesso!", true);

            // Esconde formulário de edição e recarrega tabela
            document.getElementById("formulario-editar-oficina").classList.add("d-none");
            carregarTodasOficinas();
        })
        .catch(err => {
            console.error(err);
            mostrarMensagem("Erro ao atualizar oficina.", false);
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

function formatarDataParaBR(dataISO) {
    if (!dataISO) return "";
    const data = new Date(dataISO);
    if (isNaN(data)) return "";
    const dia = String(data.getDate()).padStart(2,"0");
    const mes = String(data.getMonth()+1).padStart(2,"0");
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

function converterDataBrasilParaISO(dataBr) {
    const [dia, mes, ano] = dataBr.split("/");
    return `${ano}-${mes}-${dia}`;
}


function carregarProfessores() {
    // Faz uma requisição GET ao endpoint do back-end
    fetch("/api/professores")
        .then(response => {
            if (!response.ok) {
                throw new Error("Erro ao carregar professores. Código: " + response.status);
            }
            return response.json();
        })
        .then(professores => {
            const select = document.getElementById("professor-id");

            // Limpa as opções anteriores (caso o script rode mais de uma vez)
            select.innerHTML = '<option value="">Selecione o professor</option>';

            // Cria uma <option> para cada professor retornado do backend
            professores.forEach(prof => {
                const option = document.createElement("option");
                option.value = prof.id;      // o ID é o value (vai pro back no submit)
                option.textContent = prof.nome; // o nome aparece pro usuário
                select.appendChild(option);
            });
        })
        .catch(error => {
            console.error("Erro ao buscar professores:", error);
            alert("Não foi possível carregar a lista de professores.");
        });
}


// BUSCA DE OFICINA POR ID
document.addEventListener('DOMContentLoaded', () => {
    const btnBuscar = document.getElementById('btn-buscar-oficina');
    const btnLimpar = document.getElementById('btn-limpar-busca');
    const inputBusca = document.getElementById('input-busca-id');
    const tabela = document.getElementById('tabela-oficina-container');
    const tabelaContainer = document.getElementById('tabela-container');
    const mensagemTabela = document.getElementById('mensagem-tabela');

    btnBuscar.addEventListener('click', () => {
        const id = inputBusca.value.trim();
        if (!id) {
            alert('Digite um ID válido para buscar.');
            return;
        }

        // Faz a requisição para buscar a oficina por ID
        fetch(`http://localhost:8080/apis/oficina/${id}`) // ajuste a URL conforme sua API
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
});
