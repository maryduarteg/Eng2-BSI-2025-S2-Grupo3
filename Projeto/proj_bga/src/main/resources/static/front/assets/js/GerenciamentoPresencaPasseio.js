document.addEventListener("DOMContentLoaded", () => {

    const API = "http://localhost:8080/apis/presenca/passeio";

    const selectPasseioData = document.getElementById("select-passeio-data");
    const campoPesquisa = document.getElementById("pesquisar-passeio");
    const tabelaAlunos = document.querySelector("#tabela-alunos tbody");
    const btnSalvar = document.getElementById("btn-salvar-faltas");
    const mensagem = document.getElementById("mensagem-chamada");

    // Botão de alterar chamada
    const btnAlterar = document.createElement("button");
    btnAlterar.textContent = "✏️ Alterar chamada";
    btnAlterar.id = "btn-alterar";
    btnAlterar.className = "btn btn-warning btn-lg w-100 mt-3";
    btnAlterar.style.display = "none";
    selectPasseioData.parentElement.insertAdjacentElement("afterend", btnAlterar);

    let listaPasseios = [];

    // 🔹 Carrega datas dos passeios
    async function carregarDatasPasseios() {
        try {
            const lista = await fetch(`${API}/datas`).then(r => r.json());
            listaPasseios = [];

            for (const item of lista) {
                const res = await fetch(`${API}/chamada-feita/${item.dmp_id}`).then(r => r.json());
                listaPasseios.push({
                    ...item,
                    concluida: res.chamadaFeita
                });
            }

            renderizarPasseios(listaPasseios);
        } catch {
            mensagem.style.color = "red";
            mensagem.textContent = "Erro ao carregar datas dos passeios.";
        }
    }

    function renderizarPasseios(lista) {
        selectPasseioData.innerHTML = `<option value="">Selecione um passeio e data</option>`;
        lista.forEach(item => {
            const opt = document.createElement("option");
            opt.value = item.dmp_id;
            opt.textContent = item.concluida
                ? `${item.descricao} (Concluído)`
                : item.descricao;
            opt.dataset.concluida = item.concluida;
            selectPasseioData.appendChild(opt);
        });
    }

    // 🔍 Filtro em tempo real
    campoPesquisa.addEventListener("input", () => {
        const termo = campoPesquisa.value.toLowerCase();
        const filtradas = listaPasseios.filter(item =>
            item.descricao.toLowerCase().includes(termo)
        );
        renderizarPasseios(filtradas);
    });

    // 🔹 Mudança de passeio/data
    selectPasseioData.addEventListener("change", async function () {
        const dmp_id = parseInt(this.value);
        const concluida = this.selectedOptions[0]?.dataset.concluida === "true";

        tabelaAlunos.innerHTML = "";
        mensagem.textContent = "";
        btnSalvar.style.display = "none";
        btnAlterar.style.display = "none";

        if (!dmp_id || isNaN(dmp_id)) return;

        try {
            const lista = await fetch(`${API}/alunos/${dmp_id}`).then(r => r.json());

            if (!lista || lista.length === 0) {
                mensagem.style.color = "orange";
                mensagem.textContent = "Nenhum aluno encontrado para esta data.";
                return;
            }

            tabelaAlunos.innerHTML = "";

            if (concluida) {
                // Chamada já concluída — mostra status com emojis
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
                // Chamada ainda não feita — checkboxes
                lista.forEach(a => {
                    tabelaAlunos.innerHTML += `
                        <tr>
                            <td>${a.nome}</td>
                            <td><input type="checkbox" class="chk-falta" data-alu="${a.alu_id}" ${a.faltou ? "checked" : ""}></td>
                        </tr>
                    `;
                });
                btnSalvar.style.display = "inline-block";
            }

        } catch (err) {
            console.error(err);
            mensagem.style.color = "red";
            mensagem.textContent = "Erro ao carregar alunos.";
        }
    });

    // 🔹 Registrar novas faltas
    btnSalvar.addEventListener("click", async () => {
        const dmp_id = parseInt(selectPasseioData.value);
        mensagem.textContent = "";

        if (!dmp_id || isNaN(dmp_id)) {
            mensagem.style.color = "red";
            mensagem.textContent = "Selecione um passeio e data primeiro.";
            return;
        }

        const faltas = document.querySelectorAll(".chk-falta:checked");

        if (faltas.length === 0) {
            mensagem.style.color = "orange";
            mensagem.textContent = "Nenhuma falta marcada.";
            return;
        }

        try {
            await Promise.all(Array.from(faltas).map(chk => {
                const dto = { idAluno: parseInt(chk.dataset.alu), idDia: dmp_id };
                return fetch(`${API}`, {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(dto)
                });
            }));

            mensagem.style.color = "green";
            mensagem.textContent = "Faltas registradas com sucesso ✅";
            tabelaAlunos.innerHTML = "";
            btnSalvar.style.display = "none";
            selectPasseioData.value = "";

        } catch {
            mensagem.style.color = "red";
            mensagem.textContent = "Erro ao registrar faltas.";
        }
    });

    // 🔹 Alterar chamadas concluídas
    btnAlterar.addEventListener("click", async () => {
        const dmp_id = parseInt(selectPasseioData.value);
        const statusCells = document.querySelectorAll(".status");

        if (btnAlterar.textContent.includes("✏️")) {
            // Entrar no modo edição
            btnAlterar.textContent = "Salvar alterações";
            btnAlterar.classList.replace("btn-warning", "btn-success");

            statusCells.forEach(td => {
                const faltou = td.dataset.faltou === "true";
                td.innerHTML = `
                    <select class="edit-status" data-alu="${td.dataset.alu}" data-original="${faltou}">
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

                if (novoFaltou !== originalFaltou) {
                    if (novoFaltou) {
                        promises.push(fetch(`${API}`, {
                            method: "POST",
                            headers: { "Content-Type": "application/json" },
                            body: JSON.stringify({ idAluno: alu_id, idDia: dmp_id })
                        }));
                    } else {
                        promises.push(fetch(`${API}/${alu_id}/${dmp_id}`, { method: "DELETE" }));
                    }
                }
            });

            try {
                await Promise.all(promises);
                mensagem.style.color = "green";
                mensagem.textContent = "Alterações salvas com sucesso ✅";
                btnAlterar.textContent = "✏️ Alterar chamada";
                btnAlterar.classList.replace("btn-success", "btn-warning");
                selectPasseioData.dispatchEvent(new Event("change")); // recarrega a lista
            } catch {
                mensagem.style.color = "red";
                mensagem.textContent = "Erro ao salvar alterações.";
            }
        }
    });

    carregarDatasPasseios();
});
