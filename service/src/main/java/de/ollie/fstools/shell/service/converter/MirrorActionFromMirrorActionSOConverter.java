package de.ollie.fstools.shell.service.converter;

import org.springframework.stereotype.Component;

import de.ollie.fstools.mirror.MirrorAction;
import de.ollie.fstools.mirror.MirrorAction.ActionType;
import de.ollie.fstools.shell.service.so.MirrorActionSO;
import de.ollie.fstools.shell.service.so.MirrorActionSO.ActionTypeSO;

/**
 * A converter which converts MirrorAction service objects into MirrorAction objects.
 *
 * @author ollie (15.09.2020)
 */
@Component
public class MirrorActionFromMirrorActionSOConverter {

	public MirrorAction convert(MirrorActionSO so) {
		if (so == null) {
			return null;
		}
		return new MirrorAction() //
				// DifferenceType ?!?
				.setSourceFileName(so.getSourceFileName()) //
				.setTargetFileName(so.getTargetFileName()) //
				.setType(getType(so.getType())) //
		;
	}

	private ActionType getType(ActionTypeSO model) {
		return model == null //
				? null //
				: ActionType.valueOf(model.name());
	}

}