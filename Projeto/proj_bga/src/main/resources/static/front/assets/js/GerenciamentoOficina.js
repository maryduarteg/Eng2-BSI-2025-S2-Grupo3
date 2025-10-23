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
        console.error("Erro: Um dos IDs do formulário ou botão não foi encontrado.");
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

function cadastrarOficina(event) {
    event.preventDefault();

    const form = document.getElementById("formOficina");
    if (!form.checkValidity()) {
        form.classList.add("was-validated");
        return false;
    }

    const oficina = {
        id: 0,
        nome: form.nome.value.trim(),
        professorId: parseInt(form.professorId.value),
        dataInicio: converterDataBrasilParaISO(form.dataInicio.value),
        dataFim: converterDataBrasilParaISO(form.dataFim.value),
        horaInicio: form.horaInicio.value,
        horaFim: form.horaFim.value
    };
    fetch("http://localhost:8080/apis/oficina",
        {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams(oficina),
    })        .then(response => response.json())
        .then(res => {
            if(res.erro) {
                mostrarMensagem("Erro: " + res.erro, false);
                return;
            }
            mostrarMensagem("Sucesso! A oficina foi cadastrado.", true);
            form.reset();
            form.classList.remove("was-validated");
        })
        .catch(error => {
            console.error("Erro ao cadastrar:", error);
            mostrarMensagem("Erro ao cadastrar oficina!", false);
        });
    return false;
}


function carregarTodasOficinas() {
    const tbody = document.getElementById("tabela-oficina-container");
    const msgContainer = document.getElementById("mensagem-tabela");

    if (!tbody || !msgContainer) {
        console.error("ERRO CRÍTICO: Elemento HTML da Tabela não encontrado.");
        return;
    }

    tbody.innerHTML = '';
    msgContainer.textContent = "Carregando todas as oficinas...";

    fetch("http://localhost:8080/apis/oficina")
        .then(response => {
            if (response.status === 400) {
                return response.json().then(data => {
                    msgContainer.textContent = data.mensagem || "Nenhuma oficina cadastrada.";
                    return [];
                });
            }
            if (!response.ok) {
                throw new Error('Falha na resposta da rede: ' + response.statusText);
            }
            return response.json();
        })
        .then(listaOficina => {
            if (listaOficina.length === 0) {
                msgContainer.textContent = "Nenhuma oficina cadastrada.";
                return;
            }

            msgContainer.textContent = '';

            listaOficina.forEach(oficina => {
                const row = tbody.insertRow();

                row.innerHTML = `
                        <td>${oficina.id}</td>
                        <td>${formatarDataParaBR(oficina.dataInicio)}</td>
                        <td>${formatarDataParaBR(oficina.dataFim)}</td>
                        <td>${oficina.horaInicio}</td>
                        <td>${oficina.horaFim}</td>
                        <td>${oficina.professorId}</td>
                        <td>${oficina.ativo}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-info"> //botão de editar
                                <i class="fas fa-edit"> </i>
                            </button>
                        </td>
                        <td>
                            <button class="btn btn-sm btn-danger"> // botão de excluir
                                <i class="fas fa-trash"> </i>
                            </button>
                        </td>
                    `;
            });
        })
        .catch(error => {
            console.error("Erro ao carregar oficinas:", error);
            msgContainer.textContent = "Erro ao carregar dados do servidor.";
        });
}


function mostrarMensagem(texto, sucesso) {
    const container = document.getElementById("mensagem-oficina");
    if(container) {
        container.innerHTML = texto;
        container.className = 'mensagem ' + (sucesso ? 'sucesso' : 'erro');
    }
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