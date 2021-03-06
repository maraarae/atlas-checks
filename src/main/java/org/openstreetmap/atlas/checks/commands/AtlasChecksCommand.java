package org.openstreetmap.atlas.checks.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.openstreetmap.atlas.utilities.runtime.FlexibleCommand;
import org.openstreetmap.atlas.utilities.runtime.FlexibleSubCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

/**
 * Shell for running atlas-checks commands. Run this command with no arguments to learn more about
 * it.
 *
 * @author bbreithaupt
 */
public class AtlasChecksCommand extends FlexibleCommand
{
    private static final Logger logger = LoggerFactory.getLogger(AtlasChecksCommand.class);

    public static void main(final String... args)
    {
        final AtlasChecksCommand command = new AtlasChecksCommand(args);
        try
        {
            command.runWithoutQuitting(args);
        }
        catch (final RuntimeException e)
        {
            logger.error("Unable to runWithoutQuitting for class {} with message {}",
                    "AtlasChecksCommand", e.getMessage());
            command.printUsageAndExit(1);
        }
    }

    public AtlasChecksCommand(final String... args)
    {
        super(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Stream<Class<? extends FlexibleSubCommand>> getSupportedCommands()
    {
        final List<Class<? extends FlexibleSubCommand>> returnValue = new ArrayList<>();

        try (ScanResult scanResult = new ClassGraph().enableAllInfo()
                .whitelistPackages(AtlasChecksCommand.class.getPackage().getName()).scan())
        {
            final ClassInfoList classInfoList = scanResult
                    .getClassesImplementing(FlexibleSubCommand.class.getName());
            classInfoList.loadClasses()
                    .forEach(klass -> returnValue.add((Class<? extends FlexibleSubCommand>) klass));
        }
        return returnValue.stream();
    }
}
