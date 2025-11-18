
document.addEventListener("DOMContentLoaded", () => {

    const API = "http://localhost:8080/apis/presenca/oficina";

    const selectOficinaData = document.getElementById("select-oficina-data");
    const campoPesquisaOficina = document.getElementById("pesquisar-oficina");
    const tabelaAlunos = document.querySelector("#tabela-alunos tbody");
    const btnSalvar = document.getElementById("btn-salvar-faltas");
    const mensagem = document.getElementById("mensagem-chamada");

    // 🔹 Novo botão de alterar
    const btnAlterar = document.createElement("button");
    btnAlterar.textContent = "✏️ Alterar chamada";
    btnAlterar.id = "btn-alterar";
    btnAlterar.className = "btn btn-warning btn-lg w-100 mt-3"; // grande e estiloso
    btnAlterar.style.display = "none"; // começa oculto

    // Insere o botão logo abaixo do select e acima da tabela
    selectOficinaData.parentElement.insertAdjacentElement("afterend", btnAlterar);

    // Lista local para pesquisa
    let listaOficinas = [];

    // 🔹 Carrega oficinas e marca as concluídas
    async function carregarDatasOficinas() {
        try {
            const lista = await fetch(`${API}/datas`).then(r => r.json());
            listaOficinas = [];

            for (const item of lista) {
                const res = await fetch(`${API}/chamada-feita/${item.dmf_id}`).then(r => r.json());
                listaOficinas.push({
                    ...item,
                    concluida: res.chamadaFeita
                });
            }

            renderizarOficinas(listaOficinas);
        } catch (e) {
            mensagem.textContent = "Erro ao carregar datas das oficinas.";
        }
    }

    // 🔹 Renderiza o select com base na lista filtrada
    function renderizarOficinas(lista) {
        selectOficinaData.innerHTML = `<option value="">Selecione uma oficina e data</option>`;
        lista.forEach(item => {
            const opt = document.createElement("option");
            opt.value = item.dmf_id;
            opt.textContent = item.concluida
                ? `${item.descricao} (Concluída)`
                : item.descricao;
            opt.dataset.concluida = item.concluida;
            selectOficinaData.appendChild(opt);
        });
    }

    // 🔍 Filtro em tempo real
    campoPesquisaOficina.addEventListener("input", () => {
        const termo = campoPesquisaOficina.value.toLowerCase();
        const filtradas = listaOficinas.filter(item =>
            item.descricao.toLowerCase().includes(termo)
        );
        renderizarOficinas(filtradas);
    });

    // 🔹 Evento de troca de oficina/data
    selectOficinaData.addEventListener("change", function () {
        const dmf_id = parseInt(this.value);
        const concluida = this.selectedOptions[0]?.dataset.concluida === "true";

        mensagem.textContent = "";
        tabelaAlunos.innerHTML = "";
        btnSalvar.style.display = "none";
        btnAlterar.style.display = "none";

        if (!dmf_id) return;

        fetch(`${API}/alunos/${dmf_id}`)
            .then(r => r.json())
            .then(lista => {
                tabelaAlunos.innerHTML = "";
                if (concluida) {
                    // 🔸 Oficina concluída — mostra status com emojis
                    lista.forEach(a => {
                        tabelaAlunos.innerHTML += `
                            <tr>
                                <td>${a.nome}</td>
                                <td class="status" data-alu="${a.alu_id}" data-faltou="${a.faltou}">
                                    ${a.faltou ? "❌ Faltou" : "✅ Presente"}
                                </td>
                            </tr>
                        `;
                    });
                    btnAlterar.style.display = "inline-block";
                } else {
                    // 🔸 Oficina ainda não concluída — checkboxes normais
                    lista.forEach(a => {
                        tabelaAlunos.innerHTML += `
                            <tr>
                                <td>${a.nome}</td>
                                <td><input type="checkbox" class="chk-falta" data-alu="${a.alu_id}"></td>
                            </tr>
                        `;
                    });
                    btnSalvar.style.display = "inline-block";
                }
            })
            .catch(() => {
                mensagem.textContent = "Erro ao carregar alunos.";
            });
    });

    // 🔹 Função para registrar faltas (modo normal)
    btnSalvar.addEventListener("click", () => {
        const dmf_id = parseInt(selectOficinaData.value);
        mensagem.textContent = "";

        if (!dmf_id) {
            mensagem.textContent = "Selecione uma oficina e data primeiro.";
            return;
        }

        const faltas = document.querySelectorAll(".chk-falta:checked");
        if (faltas.length === 0) {
            mensagem.textContent = "Nenhuma falta marcada.";
            return;
        }

        Promise.all(Array.from(faltas).map(chk => {
            const dto = {
                idAluno: parseInt(chk.dataset.alu),
                idDia: dmf_id
            };
            return fetch(`${API}`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify(dto)
            });
        }))
            .then(() => {
                mensagem.style.color = "green";
                mensagem.textContent = "Faltas registradas com sucesso ✅";
                selectOficinaData.value = "";
                tabelaAlunos.innerHTML = "";
                btnSalvar.style.display = "none";
            })
            .catch(() => {
                mensagem.style.color = "red";
                mensagem.textContent = "Erro ao registrar faltas.";
            });
    });

    // 🔹 Modo de edição das chamadas concluídas
    btnAlterar.addEventListener("click", () => {
        const dmf_id = parseInt(selectOficinaData.value);
        const statusCells = document.querySelectorAll(".status");

        if (btnAlterar.textContent.includes("✏️")) {
            // Entrar no modo edição
            btnAlterar.textContent = "Salvar alterações";
            btnAlterar.classList.replace("btn-warning", "btn-success");

            statusCells.forEach(td => {
                const faltou = td.dataset.faltou === "true";
                td.innerHTML = `
                <select class="edit-status" 
                        data-alu="${td.dataset.alu}" 
                        data-original="${faltou}">
                    <option value="false" ${!faltou ? "selected" : ""}>✅ Presente</option>
                    <option value="true" ${faltou ? "selected" : ""}>❌ Faltou</option>
                </select>
            `;
            });
        } else {
            // Salvar alterações
            const selects = document.querySelectorAll(".edit-status");
            const promises = [];

            selects.forEach(sel => {
                const alu_id = parseInt(sel.dataset.alu);
                const novoFaltou = sel.value === "true";
                const originalFaltou = sel.dataset.original === "true";

                // Só faz algo se mudou!
                if (novoFaltou !== originalFaltou) {
                    if (novoFaltou) {
                        // Registrar nova falta
                        promises.push(fetch(`${API}`, {
                            method: "POST",
                            headers: {"Content-Type": "application/json"},
                            body: JSON.stringify({ idAluno: alu_id, idDia: dmf_id })
                        }));
                    } else {
                        // Remover falta
                        promises.push(fetch(`${API}/${alu_id}/${dmf_id}`, { method: "DELETE" }));
                    }
                }
            });

            // Executa apenas as mudanças
            Promise.all(promises)
                .then(() => {
                    mensagem.style.color = "green";
                    mensagem.textContent = "Alterações salvas com sucesso ✅";
                    btnAlterar.textContent = "✏️ Alterar chamada";
                    btnAlterar.classList.replace("btn-success", "btn-warning");
                    selectOficinaData.dispatchEvent(new Event("change")); // recarrega
                })
                .catch(() => {
                    mensagem.style.color = "red";
                    mensagem.textContent = "Erro ao salvar alterações.";
                });
        }
    });

    carregarDatasOficinas();
});
