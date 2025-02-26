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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zetaxii.speseMensili.dto.AzioneDTO;
import com.zetaxii.speseMensili.dto.TotaliDTO;
import com.zetaxii.speseMensili.service.AzioneService;

@RestController
@RequestMapping("/api/azioni")
public class AzioneController {

	@Autowired
	private AzioneService azioneService;

	// GET: /api/azioni/getAll?user="nome" - Ottieni tutte le azioni
	@GetMapping("/getAll")
	public ResponseEntity<List<AzioneDTO>> getAllAzioni(@RequestParam String user) {
		List<AzioneDTO> azioni = azioneService.getAllAzioni(user);
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}
	
	// GET: /api/azioni/getTotali?user="nome" - recupera tutti i totali (saldo attuale, guadagnati, spesi)
    @GetMapping("/getTotali")
    public ResponseEntity<TotaliDTO> getTotali(@RequestParam String user) {
        TotaliDTO totali = azioneService.calcolaTotali(user);
        return new ResponseEntity<>(totali, HttpStatus.OK);
    }	

	// GET: /api/azioni/getByMeseAnno/{mese}/{anno}?user="nome" - filtra per mese e anno
	@GetMapping("/getByMeseAnno/{mese}/{anno}")
	public ResponseEntity<List<AzioneDTO>> getAzioniByMeseAnno(@PathVariable String mese, @PathVariable Integer anno, @RequestParam String user) {
		List<AzioneDTO> azioni = azioneService.getAzioniByMeseAnnoOrdinate(mese, anno, user);
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}
	
	// GET: /api/azioni/getByAnno/{anno}?user="nome" - filtra per anno
	@GetMapping("/getByAnno/{anno}")
	public ResponseEntity<List<AzioneDTO>> getAzioniByAnno(@PathVariable Integer anno, @RequestParam String user) {
		List<AzioneDTO> azioni = azioneService.getAzioniByAnnoOrdinate(anno, user);
		return new ResponseEntity<>(azioni, HttpStatus.OK);
	}	

	// GET: /api/azioni/searchByFilter?user="nome" - Filtra per i campi inseriti
	@GetMapping("/searchByFilter")
	public ResponseEntity<List<AzioneDTO>> searchByFilter(@RequestBody AzioneDTO azioneDTO, @RequestParam String user) {
	    List<AzioneDTO> azioni = azioneService.searchByFilter(azioneDTO, user);
	    return new ResponseEntity<>(azioni, HttpStatus.OK);
	}

	// GET: /api/azioni/get/{id}?user="nome" - Ottieni una singola azione per ID
	@GetMapping("/get/{id}")
	public ResponseEntity<AzioneDTO> getAzioneById(@PathVariable Long id, @RequestParam String user) {
		AzioneDTO azioneDTO = azioneService.getAzioneById(id, user);
		return new ResponseEntity<>(azioneDTO, HttpStatus.OK);
	}

	// POST: /api/azioni/create?user="nome" - Crea una nuova azione
	@PostMapping("/create")
	public ResponseEntity<AzioneDTO> createAzione(@RequestBody AzioneDTO azioneDTO, @RequestParam String user) {
		AzioneDTO createdAzione = azioneService.createAzione(azioneDTO, user);
		return new ResponseEntity<>(createdAzione, HttpStatus.CREATED);
	}

	// PUT: /api/azioni/update/{id}?user="nome" - Aggiorna una azione esistente
	@PutMapping("/update/{id}")
	public ResponseEntity<AzioneDTO> updateAzione(@PathVariable Long id, @RequestBody AzioneDTO azioneDTO, @RequestParam String user) {
		AzioneDTO updatedAzione = azioneService.updateAzione(id, azioneDTO, user);
		return new ResponseEntity<>(updatedAzione, HttpStatus.OK);
	}

	// DELETE: /api/azioni/delete/{id}?user="nome" - Elimina una azione
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteAzione(@PathVariable Long id, @RequestParam String user) {
		azioneService.deleteAzione(id, user);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
}
