let diasExcluidos = [];

const btnExcluir = document.getElementById("btn-salvar-excluir");
btnExcluir.addEventListener("click",()=>{excluirDias()});
document.addEventListener("DOMContentLoaded", async () => {
    const tabelaContainer = document.getElementById("tabela-container");
    const tabelaOficina = document.getElementById("tabela-oficina-container");
    const msg = document.getElementById("mensagem-tabela-oficina");

    tabelaContainer.classList.remove("d-none");
    msg.textContent = "Carregando oficinas...";

    try {
        // busca oficinas
        const resp = await fetch("http://localhost:8080/apis/ofertaOficina");
        if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
        const listaOficinas = await resp.json();

        // busca professores uma vez só
        const respProf = await fetch("http://localhost:8080/apis/ofertaOficina/professores");
        if (!respProf.ok) throw new Error(`Erro HTTP: ${respProf.status}`);
        const listaProfessores = await respProf.json();

        tabelaOficina.innerHTML = "";
        msg.textContent = "";

        if (!listaOficinas || listaOficinas.length === 0) {
            msg.textContent = "Nenhuma oficina encontrada.";
            return;
        }

        console.log(listaOficinas);
        for (const oficina of listaOficinas) {
            if (oficina.ativo !== 'N') {
                const linha = document.createElement("tr");

                const criarColuna = (texto) => {
                    const td = document.createElement("td");
                    td.innerHTML = texto ?? "—";
                    linha.append(td);
                };

                criarColuna(oficina.idOficina);
                criarColuna(oficina.nome);
                criarColuna(formatarDataParaBR(oficina.data_inicio));
                criarColuna(formatarDataParaBR(oficina.data_fim));
                criarColuna(oficina.hora_inicio);
                criarColuna(oficina.hora_termino);

                // coluna do professor
                const tdProf = document.createElement("td");
                const prof = listaProfessores.find(p => p.id == oficina.pde_id);
                tdProf.textContent = prof ? prof.nome : "—";
                linha.append(tdProf);

                // botão editar
                const tdEditar = document.createElement("td");
                const btnEditar = document.createElement("button");
                btnEditar.classList.add("btn", "btn-sm", "btn-primary");
                btnEditar.textContent = "Editar";

                btnEditar.dataset.id = oficina.idOficina;
                btnEditar.dataset.nome = oficina.nome;
                btnEditar.dataset.inicio = oficina.hora_inicio;
                btnEditar.dataset.fim = oficina.hora_termino;

                btnEditar.addEventListener("click", () => {
                    console.log(`Editar oficina ${oficina.idOficina}`);
                    carregarEdicaoDias(btnEditar.dataset.id,
                        btnEditar.dataset.nome,
                        btnEditar.dataset.inicio, btnEditar.dataset.fim);

                });
                tdEditar.append(btnEditar);
                linha.append(tdEditar);

                tabelaOficina.appendChild(linha);
            }
        }
    } catch (err) {
        console.error("Erro oficinas:", err);
        msg.textContent = "Erro ao carregar oficinas.";
    }
});

function formatarDataParaBR(dataISO) {
    if (!dataISO) return "";
    const data = new Date(dataISO);
    if (isNaN(data)) return "";
    const dia = String(data.getDate()).padStart(2, "0");
    const mes = String(data.getMonth() + 1).padStart(2, "0");
    const ano = data.getFullYear();
    return `${dia}/${mes}/${ano}`;
}

function carregarEdicaoDias(id, nome, inicio, fim) {
    const tabelaDias = document.getElementById("tabela-dias-container");
    const bodyDias = document.getElementById("tabela-dias-container-body");
    tabelaDias.classList.remove("d-none");

    document.getElementById("nomeOficina").textContent = nome;
    document.getElementById("idOficina").textContent = id;
    document.getElementById("horainicio").textContent = inicio;
    document.getElementById("horafinal").textContent = fim;
    document.getElementById("mensagem-tabela-dias").classList.add("d-none");

    diasExcluidos = [];
    bodyDias.innerHTML = "";

    fetch("http://localhost:8080/apis/dias")
        .then(resp => {
            if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
            return resp.json();
        })
        .then(listaDias => {
            if (!listaDias || listaDias.length === 0)
                return;

            listaDias.forEach(dia => {
                if (dia.idofc == id) { // se a data pertence à oficina
                    const linha = document.createElement("tr");

                    // coluna da data
                    const tdData = document.createElement("td");
                    tdData.textContent = formatarDataParaBR(dia.data) ?? "—";
                    linha.appendChild(tdData);

                    // coluna do checkbox
                    const tdCheck = document.createElement("td");
                    const check = document.createElement("input");
                    check.type = "checkbox";
                    check.dataset.id = dia.id;

                    // adiciona/remover ID da lista de exclusão
                    check.addEventListener("change", () => {
                        const idNum = parseInt(check.dataset.id);
                        if (check.checked) {
                            if (!diasExcluidos.includes(idNum))
                                diasExcluidos.push(idNum);
                        } else {
                            diasExcluidos = diasExcluidos.filter(x => x !== idNum);
                        }
                        console.log("Dias selecionados pra excluir:", diasExcluidos);
                    });

                    tdCheck.appendChild(check);
                    linha.appendChild(tdCheck);
                    bodyDias.appendChild(linha);
                }
            });
        })
        .catch(err => console.error("Erro capturado:", err));
}


function excluirDias()
{
    diasExcluidos.forEach(i => {
        fetch(`http://localhost:8080/apis/dias/${i}`, { method: "DELETE" })
            .then(response => {
                console.log("Status:", response.status);
                if (!response.ok) throw new Error("Erro HTTP " + response.status);
                return response.text(); // ou response.json() se o backend devolver JSON
            })
            .then(data => console.log("Resposta:", data))
            .catch(err => console.error("Erro ao excluir:", err));

    });
    alert("Datas atualizadas!");
    carregarEdicaoDias(
        document.getElementById("idOficina").textContent,
        document.getElementById("nomeOficina").textContent,

    document.getElementById("horainicio").textContent,
    document.getElementById("horafinal").textContent);
}

//add filtros
let listaOficinas = [];
let listaProfessores = [];

// filtro por nome do professor
document.getElementById("filtro-professor").addEventListener("keyup", (e) => {
    const filtro = e.target.value.toLowerCase();
    const tabelaOficina = document.getElementById("tabela-oficina-container");

    // limpa tabela
    tabelaOficina.innerHTML = "";

    // percorre oficinas e reexibe só as que casam com o filtro
    for (const oficina of listaOficinas) {
        if (oficina.ativo !== 'N') {
            const prof = listaProfessores.find(p => p.id == oficina.pde_id);
            const nomeProf = prof ? prof.nome.toLowerCase() : "";

            if (nomeProf.includes(filtro)) {
                const linha = document.createElement("tr");

                const criarColuna = (texto) => {
                    const td = document.createElement("td");
                    td.innerHTML = texto ?? "—";
                    linha.append(td);
                };

                criarColuna(oficina.idOficina);
                criarColuna(oficina.nome);
                criarColuna(formatarDataParaBR(oficina.data_inicio));
                criarColuna(formatarDataParaBR(oficina.data_fim));
                criarColuna(oficina.hora_inicio);
                criarColuna(oficina.hora_termino);

                const tdProf = document.createElement("td");
                tdProf.textContent = prof ? prof.nome : "—";
                linha.append(tdProf);

                const tdEditar = document.createElement("td");
                const btnEditar = document.createElement("button");
                btnEditar.classList.add("btn", "btn-sm", "btn-primary");
                btnEditar.textContent = "Editar";

                btnEditar.dataset.id = oficina.idOficina;
                btnEditar.dataset.nome = oficina.nome;
                btnEditar.dataset.inicio = oficina.hora_inicio;
                btnEditar.dataset.fim = oficina.hora_termino;

                btnEditar.addEventListener("click", () => {
                    carregarEdicaoDias(
                        btnEditar.dataset.id,
                        btnEditar.dataset.nome,
                        btnEditar.dataset.inicio,
                        btnEditar.dataset.fim
                    );
                });

                tdEditar.append(btnEditar);
                linha.append(tdEditar);

                tabelaOficina.appendChild(linha);
            }
        }
    }
});

document.addEventListener("DOMContentLoaded", async () => {
    const tabelaContainer = document.getElementById("tabela-container");
    const tabelaOficina = document.getElementById("tabela-oficina-container");
    const msg = document.getElementById("mensagem-tabela-oficina");

    tabelaContainer.classList.remove("d-none");
    msg.textContent = "Carregando oficinas...";

    try {
        const resp = await fetch("http://localhost:8080/apis/ofertaOficina");
        if (!resp.ok) throw new Error(`Erro HTTP: ${resp.status}`);
        listaOficinas = await resp.json();

        const respProf = await fetch("http://localhost:8080/apis/ofertaOficina/professores");
        if (!respProf.ok) throw new Error(`Erro HTTP: ${respProf.status}`);
        listaProfessores = await respProf.json();

        tabelaOficina.innerHTML = "";
        msg.textContent = "";

        if (!listaOficinas || listaOficinas.length === 0) {
            msg.textContent = "Nenhuma oficina encontrada.";
            return;
        }

        // exibir todas inicialmente
        document.getElementById("filtro-professor").dispatchEvent(new Event("keyup"));

    } catch (err) {
        console.error("Erro oficinas:", err);
        msg.textContent = "Erro ao carregar oficinas.";
    }
});
document.getElementById("filtro-nome").addEventListener("keyup", (e) => {
    const filtro = e.target.value.toLowerCase();
    const tabelaOficina = document.getElementById("tabela-oficina-container");

    tabelaOficina.innerHTML = "";

    if (!listaOficinas || listaOficinas.length === 0) {
        const msg = document.createElement("tr");
        msg.innerHTML = `<td colspan="7" class="text-center text-muted">Nenhuma oficina carregada.</td>`;
        tabelaOficina.appendChild(msg);
        return;
    }

    let encontrou = false;

    for (const oficina of listaOficinas) {
        if (oficina.ativo !== 'N' && oficina.nome && oficina.nome.toLowerCase().includes(filtro)) {
            encontrou = true;
            const linha = document.createElement("tr");

            const criarColuna = (texto) => {
                const td = document.createElement("td");
                td.innerHTML = texto ?? "—";
                linha.append(td);
            };

            criarColuna(oficina.idOficina);
            criarColuna(oficina.nome);
            criarColuna(formatarDataParaBR(oficina.data_inicio));
            criarColuna(formatarDataParaBR(oficina.data_fim));
            criarColuna(oficina.hora_inicio);
            criarColuna(oficina.hora_termino);

            // professor
            const tdProf = document.createElement("td");
            const prof = listaProfessores.find(p => p.id == oficina.pde_id);
            tdProf.textContent = prof ? prof.nome : "—";
            linha.append(tdProf);

            // botão editar
            const tdEditar = document.createElement("td");
            const btnEditar = document.createElement("button");
            btnEditar.classList.add("btn", "btn-sm", "btn-primary");
            btnEditar.textContent = "Editar";

            btnEditar.dataset.id = oficina.idOficina;
            btnEditar.dataset.nome = oficina.nome;
            btnEditar.dataset.inicio = oficina.hora_inicio;
            btnEditar.dataset.fim = oficina.hora_termino;

            btnEditar.addEventListener("click", () => {
                carregarEdicaoDias(
                    btnEditar.dataset.id,
                    btnEditar.dataset.nome,
                    btnEditar.dataset.inicio,
                    btnEditar.dataset.fim
                );
            });

            tdEditar.append(btnEditar);
            linha.append(tdEditar);

            tabelaOficina.appendChild(linha);
        }
    }

    if (!encontrou) {
        const msg = document.createElement("tr");
        msg.innerHTML = `<td colspan="7" class="text-center text-muted">Nenhuma oficina encontrada.</td>`;
        tabelaOficina.appendChild(msg);
    }
});


