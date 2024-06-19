import React  from "react";
import { Text } from "./Text";
import { Button } from "./Button";
import { Container } from "./Container";
import {Element} from "@craftjs/core";
import {ContainerSettings} from "./Container";
import {ContainerDefaultProps} from "./Container";
export const Card = ({background, padding = 20}) => {
    return (
        <Container background={background} padding={padding}>
            <Element id="text" canvas>
            <div className="text-only">
                <Text text="Title" fontSize={20} />
                <Text text="Subtitle" fontSize={15} />
            </div>
            </Element>
            <Element id="buttons" canvas>
            <div className="buttons-only">
                <Button size="small" text="Learn more" variant="contained" color="primary" />
            </div>
            </Element>
        </Container>
    )
}
Card.craft = {
    props: ContainerDefaultProps,
    related: {
        // Since Card has the same settings as Container, we'll just reuse ContainerSettings
        settings: ContainerSettings
    }
}
