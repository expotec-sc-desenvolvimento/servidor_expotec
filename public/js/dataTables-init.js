$.extend(true, $.fn.dataTable.defaults, {
	"oLanguage": {
		"sLengthMenu": "Registros por página: _MENU_",
        "sZeroRecords": "Nenhum registro foi encontrado",
        "sEmptyTable": "Não há dados disponíveis na tabela",
        "sInfo": "Mostrando _START_ a _END_ de _TOTAL_ registros",
        "sInfoEmpty": "Mostrando 0 a 0 de 0 registros",
        "sInfoFiltered": "(Filtrado de _MAX_ registros totais)",
        "sSearch": "Procurar : ",
        "sProcessing":"Processando...",
        "oPaginate": {
        	"sPrevious": "Anterior",
        	"sNext": "Próximo"
        }
	}
});