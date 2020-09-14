package de.ollie.fstools.shell.service.converter;

import org.springframework.stereotype.Component;

import de.ollie.fstools.mirror.MirrorAction;
import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.shell.service.so.MirrorActionSO;
import de.ollie.fstools.shell.service.so.MirrorActionSO.ActionTypeSO;

/**
 * A converter which converts MirrorAction objects into MirrorAction service objects.
 *
 * @author ollie (12.09.2020)
 */
@Component
public class MirrorActionSOFromMirrorActionConverter {

	public MirrorActionSO convert(MirrorAction model) {
		if (model == null) {
			return null;
		}
		return new MirrorActionSO() //
				.setDifferenceMessage(String.valueOf(model.getDifferenceType())) //
				.setSourceFileName(model.getSourceFileName()) //
				.setTargetFileName(model.getTargetFileName()) //
				.setType(getTypeSO(model.getType())) //
		;
	}

	private ActionTypeSO getTypeSO(ActionType model) {
		return model == null //
				? null //
				: ActionTypeSO.valueOf(model.name());
	}

}