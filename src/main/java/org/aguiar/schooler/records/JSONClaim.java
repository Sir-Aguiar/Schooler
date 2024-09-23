package org.aguiar.schooler.records;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record JSONClaim(Map<UUID, List<Claim>> claims) {

}
