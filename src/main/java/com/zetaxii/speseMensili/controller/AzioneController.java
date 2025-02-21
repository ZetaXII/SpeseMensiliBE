package com.zetaxii.speseMensili.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zetaxii.speseMensili.dto.AzioneDTO;
import com.zetaxii.speseMensili.dto.TotaliDTO;
import com.zetaxii.speseMensili.service.AzioneService;

@RestController
@RequestMapping("/api/azioni")
public class AzioneController {

	@Autowired
	private AzioneService azioneService;

	// GET: /api/azioni/getAll - Ottieni tutte le azioni
	@GetMapping("/getAll")
	public ResponseEntity<List<AzioneDTO>> getAllAzioni() {
		List<AzioneDTO> azioni = azioneService.getAllAzioni();
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}
	
	// GET: /api/azioni/getTotali - recupera tutti i totali (saldo attuale, guadagnati, spesi)
    @GetMapping("/getTotali")
    public ResponseEntity<TotaliDTO> getTotali() {
        TotaliDTO totali = azioneService.calcolaTotali();
        return new ResponseEntity<>(totali, HttpStatus.OK);
    }	

	// GET: /api/azioni/getByMeseAnno/{mese}/{anno} - filtra per mese e anno
	@GetMapping("/getByMeseAnno/{mese}/{anno}")
	public ResponseEntity<List<AzioneDTO>> getAzioniByMeseAnno(@PathVariable String mese, @PathVariable Integer anno) {
		List<AzioneDTO> azioni = azioneService.getAzioniByMeseAnnoOrdinate(mese, anno);
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}
	
	// GET: /api/azioni/getByAnno/{anno} - filtra per mese e anno
	@GetMapping("/getByAnno/{anno}")
	public ResponseEntity<List<AzioneDTO>> getAzioniByMeseAnno(@PathVariable Integer anno) {
		List<AzioneDTO> azioni = azioneService.getAzioniByAnnoOrdinate(anno);
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}	

	// GET: /api/azioni/searchByFilter - Filtra per i campi inseriti
	@GetMapping("/searchByFilter")
	public ResponseEntity<List<AzioneDTO>> searchByFilter(@RequestBody AzioneDTO azioneDTO) {
	    List<AzioneDTO> azioni = azioneService.searchByFilter(azioneDTO);
	    return new ResponseEntity<>(azioni, HttpStatus.OK);
	}

	// GET: /api/azioni/get/{id} - Ottieni una singola azione per ID
	@GetMapping("/get/{id}")
	public ResponseEntity<AzioneDTO> getAzioneById(@PathVariable Long id) {
		AzioneDTO azioneDTO = azioneService.getAzioneById(id);
		return new ResponseEntity<>(azioneDTO, HttpStatus.OK);
	}

	// POST: /api/azioni/create - Crea una nuova azione
	@PostMapping("/create")
	public ResponseEntity<AzioneDTO> createAzione(@RequestBody AzioneDTO azioneDTO) {
		AzioneDTO createdAzione = azioneService.createAzione(azioneDTO);
		return new ResponseEntity<>(createdAzione, HttpStatus.CREATED);
	}

	// PUT: /api/azioni/update/{id} - Aggiorna una azione esistente
	@PutMapping("/update/{id}")
	public ResponseEntity<AzioneDTO> updateAzione(@PathVariable Long id, @RequestBody AzioneDTO azioneDTO) {
		AzioneDTO updatedAzione = azioneService.updateAzione(id, azioneDTO);
		return new ResponseEntity<>(updatedAzione, HttpStatus.OK);
	}

	// DELETE: /api/azioni/delete/{id} - Elimina una azione
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteAzione(@PathVariable Long id) {
		azioneService.deleteAzione(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
