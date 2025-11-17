document.addEventListener("DOMContentLoaded", () => {
    const API = "http://localhost:8080/apis/fotos/oficina";
    const containerFotos = document.getElementById("container-fotos");
    const filtroOficina = document.getElementById("filtro-oficina");
    const btnBuscar = document.getElementById("btn-buscar");
    const mensagem = document.getElementById("mensagem-fotos");

    carregarFotos();

    btnBuscar.addEventListener("click", () => {
        const ofc_id = filtroOficina.value.trim();
        if (ofc_id) {
            carregarFotosPorOficina(parseInt(ofc_id));
        } else {
            carregarFotos();
        }
    });

    function carregarFotos() {
        fetch(API)
            .then(r => r.json())
            .then(fotos => {
                exibirFotos(fotos);
            })
            .catch(err => {
                mostrarMensagem("Erro ao carregar fotos: " + err.message, "danger");
            });
    }

    function carregarFotosPorOficina(ofc_id) {
        fetch(`${API}/oficina/${ofc_id}`)
            .then(r => r.json())
            .then(fotos => {
                exibirFotos(fotos);
            })
            .catch(err => {
                mostrarMensagem("Erro ao buscar fotos: " + err.message, "danger");
            });
    }

    // FUNÇÃO CORRIGIDA - estava incompleta
    function exibirFotos(fotos) {
        containerFotos.innerHTML = "";
        if (fotos.length === 0) {
            containerFotos.innerHTML = `
                <div class="col-12">
                    <div class="alert alert-info">Nenhuma foto encontrada.</div>
                </div>
            `;
            return;
        }

        fotos.forEach(foto => {
            const dataFormatada = foto.fto_data_upload ?
                new Date(foto.fto_data_upload).toLocaleDateString('pt-BR') : 'N/A';

            const card = `
                <div class="col-md-4 mb-4">
                    <div class="card">
                        <img src="data:image/jpeg;base64,${foto.fto_foto}" 
                             class="card-img-top" alt="${foto.fto_descricao}">
                        <div class="card-body">
                            <h5 class="card-title">${foto.fto_descricao}</h5>
                            <p class="card-text">
                                <strong>Data:</strong> ${dataFormatada}<br>
                                <strong>Oficina:</strong> ${foto.ofc_id}<br>
                                <strong>Dia:</strong> ${foto.dmf_id}<br>
                                <strong>Número:</strong> ${foto.fto_numero}
                            </p>
                            <button class="btn btn-danger btn-sm" 
                                    onclick="deletarFoto(${foto.ofc_id}, ${foto.dmf_id}, ${foto.fto_numero})">
                                Excluir
                            </button>
                        </div>
                    </div>
                </div>
            `;
            containerFotos.innerHTML += card;
        });
    }

    // Função global para deletar
    window.deletarFoto = function(ofc_id, dmf_id, fto_numero) {
        if (confirm("Deseja realmente excluir esta foto?")) {
            fetch(`${API}?ofc_id=${ofc_id}&dmf_id=${dmf_id}&fto_numero=${fto_numero}`, {
                method: 'DELETE'
            })
                .then(r => r.json())
                .then(res => {
                    if (res.erro) {
                        mostrarMensagem(res.erro, "danger");
                    } else {
                        mostrarMensagem("Foto excluída com sucesso!", "success");
                        carregarFotos();
                    }
                })
                .catch(err => {
                    mostrarMensagem("Erro ao excluir foto: " + err.message, "danger");
                });
        }
    };

    function mostrarMensagem(texto, tipo) {
        mensagem.textContent = texto;
        mensagem.className = `alert alert-${tipo}`;
        setTimeout(() => {
            mensagem.textContent = "";
            mensagem.className = "";
        }, 5000);
    }
});
